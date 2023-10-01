//> using lib "org.typelevel::cats-core:2.10.0"
//> using lib "com.lihaoyi::pprint:0.8.1"
//> using lib "org.polyvariant::colorize:0.3.2"
//> using option "-Wunused:imports"
import scala.quoted.Quotes
import org.polyvariant.colorize.string.ColorizedString
import cats.Eval
import cats.syntax.all.*
import scala.deriving.Mirror
import scanner.*
import util.chaining.*

scan(".=[]{},")
scan("import com.kubukoz#identifier")
scan("import co111m.kub1ukoz#ident_ifie---,_,r\nimport a")

case class GreenNode(
  kind: SyntaxKind,
  children: List[Either[GreenNode, Token]],
) {
  def cast[A](using mirror: AstNodeMirror[A]): Option[A] = mirror.cast(this)

  def allTokens: List[Token] = children.flatMap {
    _.fold(_.allTokens, _.some)
  }

  lazy val width: Int = children.foldMap(_.fold(_.width, _.text.length()))

  def print: String = {
    def go(depth: Int, self: GreenNode): String =
      "  " * depth +
        s"${self.kind}:${self.width}\n" +
        self
          .children
          .map {
            case Left(node)   => go(depth + 1, node)
            case Right(token) => "  " * (depth + 1) + token.text
          }
          .mkString("\n")

    go(0, this)
  }

}

object GreenNode {
  def builder(kind: SyntaxKind) = new GreenNodeBuilder(kind)

  def error(
    token: Token
  ): GreenNode = builder(SyntaxKind.ERROR).addChild(token).build()

  class GreenNodeBuilder(kind: SyntaxKind) {
    private var _children: Vector[Either[GreenNode, Token]] = Vector.empty

    def addChild(child: GreenNode): this.type = addChild(child.asLeft)
    def addChild(child: Token): this.type = addChild(child.asRight)

    def addChild(child: Either[GreenNode, Token]): this.type = {
      this._children :+= child
      this
    }

    def build(): GreenNode = GreenNode(
      kind = kind,
      children = _children.toList,
    )

  }

}

case class SyntaxNode(
  offset: Int,
  parent: Eval[Option[SyntaxNode]],
  green: Either[GreenNode, Token],
) {

  def children: List[SyntaxNode] = {
    def go(offset: Int, remaining: List[Either[GreenNode, Token]]): List[SyntaxNode] =
      remaining match {
        case Nil => Nil
        case one :: more =>
          SyntaxNode(offset, Eval.later(this.some), one) :: go(
            offset + one.fold(_.width, _.width),
            more,
          )
      }

    go(offset, green.fold(_.children, _ => Nil))
  }

  def width = green.fold(_.width, _.width)

  def print: String = {
    def go(depth: Int, self: SyntaxNode): String = {
      val content = self.green.fold(_ => "", t => s" \"${t.text}\"")
      "  " * depth +
        s"""${self.green.fold(_.kind, _.kind)}@${self.offset}..${self.offset + self.width}$content
           |""".stripMargin +
        self
          .children
          .map(go(depth + 1, _))
          .mkString
    }

    go(0, this)
  }

}

object SyntaxNode {

  def newRoot(
    green: GreenNode
  ): SyntaxNode = SyntaxNode(offset = 0, parent = Eval.now(None), green = green.asLeft)

}

enum SyntaxKind {
  case File
  case FQN
  case Namespace
  case Identifier
  case ERROR
}

trait AstNode[Self] { self: Product =>
  def syntax: GreenNode

  def firstChildToken(kind: TokenKind): Option[Token] = syntax.children.collectFirst {
    case Right(tok @ Token(`kind`, text)) => tok
  }

  def allChildNodes[
    N: AstNodeMirror
  ]: List[N] = syntax.children.mapFilter(_.left.toOption.flatMap(_.cast[N]))

  def firstChildNode[
    N: AstNodeMirror
  ]: Option[N] = syntax.children.collectFirstSome(_.left.toOption.flatMap(_.cast[N]))

}

trait AstNodeMirror[Self] {
  def cast(node: GreenNode): Option[Self]
}

object AstNodeMirror {

  def derived[T <: AstNode[T]](
    using m: Mirror.ProductOf[T] { type MirroredElemTypes = Tuple1[GreenNode] },
    label: ValueOf[m.MirroredLabel],
  ): AstNodeMirror[T] = {
    val matchingSyntaxKind = SyntaxKind.valueOf(label.value)

    node =>
      node.kind match {
        case `matchingSyntaxKind` => Some(m.fromProductTyped(Tuple1(node)))
        case _                    => None
      }
  }

}

// concrete

case class Identifier(syntax: GreenNode) extends AstNode[Identifier] derives AstNodeMirror {
  def value: Option[Token] = firstChildToken(TokenKind.IDENT)
}

case class Namespace(syntax: GreenNode) extends AstNode[Namespace] derives AstNodeMirror {
  def parts: List[Identifier] = allChildNodes[Identifier]
}

case class FQN(syntax: GreenNode) extends AstNode[FQN] derives AstNodeMirror {
  def namespace: Option[Namespace] = firstChildNode[Namespace]
  def name: Option[Identifier] = firstChildNode[Identifier]
}

case class Tokens(private var all: List[Token], private var cursor: Int) {

  def eof: Boolean = cursor >= all.length

  def peek(): Token = all(cursor)

  def bump(): Token = {
    val result = peek()
    cursor += 1
    result
  }

}

object Tokens {
  def apply(tokens: List[Token]): Tokens = Tokens(tokens, 0)
}

def parseIdent(
  tokens: Tokens
): GreenNode = {
  val builder = GreenNode.builder(SyntaxKind.Identifier)
  val next = tokens.bump()
  next.kind match {
    case TokenKind.IDENT => builder.addChild(next)
    case _               => builder.addChild(GreenNode.error(next))
  }
  builder.build()
}

def parseNamespace(tokens: Tokens): GreenNode = {
  val builder = GreenNode.builder(SyntaxKind.Namespace)

  var done = false

  while (!tokens.eof && !done)
    tokens.peek().kind match {
      case TokenKind.IDENT =>
        // todo: after an ident, expect dot or hash (some sort of state machine / another method in the recursive descent?)
        // if it's an ident, report an error but don't wrap in ERROR
        // otherwise, wrap in ERROR
        builder.addChild(parseIdent(tokens))

      case TokenKind.DOT =>
        // swallow token
        builder.addChild(tokens.bump())

      case TokenKind.HASH => done = true // end of namespace, move on

      case _ =>
        // skip extra/invalid tokens. we will report these in the future
        builder.addChild(GreenNode.error(tokens.bump()))
        tokens.bump()
    }

  builder.build()
}

def parseFQN(tokens: Tokens): GreenNode = {
  val builder = GreenNode.builder(SyntaxKind.FQN)
  builder.addChild(parseNamespace(tokens))
  if (tokens.peek().kind == TokenKind.HASH) {
    builder.addChild(tokens.bump())
  }
  builder.addChild(parseIdent(tokens))
  builder.build()
}

parseIdent(Tokens(TokenKind.IDENT("hello") :: Nil)).cast[Identifier].get.value

parseIdent(Tokens(TokenKind.IDENT("hello") :: TokenKind.IDENT("world") :: Nil))

parseNamespace(Tokens(Nil))
parseNamespace(Tokens(TokenKind.IDENT("hello") :: Nil))

parseNamespace(Tokens(scan("com.kubukoz.world")))
  .cast[Namespace]
  .get
  .parts
  .map(_.value)

val fqn = parseFQN(Tokens(scan("com.kubukoz#foo"))).cast[FQN]
fqn.get.namespace.get.parts.map(_.value.get)
fqn.get.name.get.value.get

//todo: this should have all tokens, even extraneous ones. Should render to the string above.
parseFQN(Tokens(scan("co111m.kub1ukoz#shrek_blob---,_,r"))).allTokens.foldMap(_.text)

parseFQN(Tokens(scan("co111m.kub1ukoz#shrek_blob---,_,r")))
parseFQN(Tokens(scan("co111m.kub1ukoz#shrek_blob---,_,r"))).print

val text =
  """|import com.kubukoz.foo#bar
     |hello
     |// this is a comment
     |// and so is this. same comment really
     |
     |//another comment
     |""".stripMargin

// scan(text)
//   .tapEach(pprint.pprintln(_))

// println(
//   scan(text)
//     .map(t =>
//       s"${Console.GREEN}${t.kind}:${Console.RESET}${t.text.replace(" ", "·").replace("\n", "⏎")}"
//     )
//     .mkString("\n")
// )
// println(scan(text).foldMap(_.text) == text)

pprint.pprintln(scan("com.kubukoz#helloworld"))
// pprint.pprintln(SyntaxNode.newRoot(parseFQN(Tokens(scan("com.kubukoz#helloworld")))).children)
println(SyntaxNode.newRoot(parseFQN(Tokens(scan("com.kubukoz#helloworld")))).print)
