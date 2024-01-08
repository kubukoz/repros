import bindgen.interface.Binding

import scala.scalanative.build.BuildTarget

def crossPlugin(
  x: sbt.librarymanagement.ModuleID
) = compilerPlugin(x.cross(CrossVersion.full))

val compilerPlugins = List(
  crossPlugin("org.polyvariant" % "better-tostring" % "0.3.17")
)

val commonSettings = Seq(
  scalaVersion := "3.3.1",
  scalacOptions --= Seq("-Xfatal-warnings"),
  scalacOptions ++= Seq(
    "-Wunused:all"
  ),
  name := "demo",
  Compile / doc / sources := Nil,
)

val app = crossProject(JVMPlatform, NativePlatform)
  .crossType(CrossType.Pure)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "io.chrisdavenport" %%% "crossplatformioapp" % "0.1.0"
    ) ++ compilerPlugins
  )
  .jvmConfigure(
    _.enablePlugins(JavaAppPackaging)
  )
  .nativeConfigure(
    _.settings(
      libraryDependencies ++= Seq(
        "com.armanbilge" %%% "epollcat" % "0.1.6"
      ),
      nativeConfig ~= (
        _.withBuildTarget(BuildTarget.libraryDynamic)
      ),
      // bindgenBinary := file(sys.env("BINDGEN_PATH")),
      // bindgenBindings := Seq(
      //   Binding
      //     .builder(file("app") / ".native" / "src" / "main" / "resources" / "pd_api.h", "pdapi")
      //     .addClangFlag(Seq("-DTARGET_SIMULATOR=1", "-DTARGET_EXTENSION=1"))
      //     .build
      // ),
    )
    // .enablePlugins(BindgenPlugin)
  )

val root = project
  .in(file("."))
  .aggregate(app.componentProjects.map(p => p: ProjectReference): _*)
