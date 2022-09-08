package com.amazonaws.dynamodb

import smithy4s.Schema
import smithy4s.Hints
import smithy4s.ShapeId
import smithy4s.schema.Schema.struct
import smithy4s.ShapeTag

case class PutItemInput(tableName: TableName, item: Map[AttributeName,AttributeValue], expected: Option[Map[AttributeName,ExpectedAttributeValue]] = None, returnValues: Option[ReturnValue] = None, returnConsumedCapacity: Option[ReturnConsumedCapacity] = None, returnItemCollectionMetrics: Option[ReturnItemCollectionMetrics] = None, conditionalOperator: Option[ConditionalOperator] = None, conditionExpression: Option[ConditionExpression] = None, expressionAttributeNames: Option[Map[ExpressionAttributeNameVariable,AttributeName]] = None, expressionAttributeValues: Option[Map[ExpressionAttributeValueVariable,AttributeValue]] = None)
object PutItemInput extends ShapeTag.Companion[PutItemInput] {
  val id: ShapeId = ShapeId("com.amazonaws.dynamodb", "PutItemInput")

  val hints : Hints = Hints(
    smithy.api.Documentation("<p>Represents the input of a <code>PutItem</code> operation.</p>"),
  )

  implicit val schema: Schema[PutItemInput] = struct(
    TableName.schema.required[PutItemInput]("TableName", _.tableName).addHints(smithy.api.Documentation("<p>The name of the table to contain the item.</p>"), smithy.api.Required()),
    PutItemInputAttributeMap.underlyingSchema.required[PutItemInput]("Item", _.item).addHints(smithy.api.Documentation("<p>A map of attribute name/value pairs, one for each attribute. Only the primary key\n            attributes are required; you can optionally provide other attribute name-value pairs for\n            the item.</p>\n        <p>You must provide all of the attributes for the primary key. For example, with a simple\n            primary key, you only need to provide a value for the partition key. For a composite\n            primary key, you must provide both values for both the partition key and the sort\n            key.</p>\n        <p>If you specify any attributes that are part of an index key, then the data types for\n            those attributes must match those of the schema in the table\'s attribute\n            definition.</p>\n        <p>Empty String and Binary attribute values are allowed. Attribute values of type String\n            and Binary must have a length greater than zero if the attribute is used as a key\n            attribute for a table or index.</p>\n\n        <p>For more information about primary keys, see <a href=\"https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.CoreComponents.html#HowItWorks.CoreComponents.PrimaryKey\">Primary Key</a> in the <i>Amazon DynamoDB Developer\n            Guide</i>.</p>\n        <p>Each element in the <code>Item</code> map is an <code>AttributeValue</code>\n            object.</p>"), smithy.api.Required()),
    ExpectedAttributeMap.underlyingSchema.optional[PutItemInput]("Expected", _.expected).addHints(smithy.api.Documentation("<p>This is a legacy parameter. Use <code>ConditionExpression</code> instead. For more\n            information, see <a href=\"https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/LegacyConditionalParameters.Expected.html\">Expected</a> in the <i>Amazon DynamoDB Developer\n            Guide</i>.</p>")),
    ReturnValue.schema.optional[PutItemInput]("ReturnValues", _.returnValues).addHints(smithy.api.Documentation("<p>Use <code>ReturnValues</code> if you want to get the item attributes as they appeared\n            before they were updated with the <code>PutItem</code> request. For\n            <code>PutItem</code>, the valid values are:</p>\n        <ul>\n            <li>\n                <p>\n                    <code>NONE</code> - If <code>ReturnValues</code> is not specified, or if its\n                    value is <code>NONE</code>, then nothing is returned. (This setting is the\n                    default for <code>ReturnValues</code>.)</p>\n            </li>\n            <li>\n                <p>\n                    <code>ALL_OLD</code> - If <code>PutItem</code> overwrote an attribute name-value\n                    pair, then the content of the old item is returned.</p>\n            </li>\n         </ul>\n        <p>The values returned are strongly consistent.</p>\n        <p>There is no additional cost associated with requesting a return value aside from the small \n            network and processing overhead of receiving a larger response. No read capacity units are \n            consumed.</p>\n        <note>\n            <p>The <code>ReturnValues</code> parameter is used by several DynamoDB operations;\n                however, <code>PutItem</code> does not recognize any values other than\n                    <code>NONE</code> or <code>ALL_OLD</code>.</p>\n        </note>")),
    ReturnConsumedCapacity.schema.optional[PutItemInput]("ReturnConsumedCapacity", _.returnConsumedCapacity),
    ReturnItemCollectionMetrics.schema.optional[PutItemInput]("ReturnItemCollectionMetrics", _.returnItemCollectionMetrics).addHints(smithy.api.Documentation("<p>Determines whether item collection metrics are returned. If set to <code>SIZE</code>,\n            the response includes statistics about item collections, if any, that were modified\n            during the operation are returned in the response. If set to <code>NONE</code> (the\n            default), no statistics are returned.</p>")),
    ConditionalOperator.schema.optional[PutItemInput]("ConditionalOperator", _.conditionalOperator).addHints(smithy.api.Documentation("<p>This is a legacy parameter. Use <code>ConditionExpression</code> instead. For more\n            information, see <a href=\"https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/LegacyConditionalParameters.ConditionalOperator.html\">ConditionalOperator</a> in the <i>Amazon DynamoDB Developer\n                Guide</i>.</p>")),
    ConditionExpression.schema.optional[PutItemInput]("ConditionExpression", _.conditionExpression).addHints(smithy.api.Documentation("<p>A condition that must be satisfied in order for a conditional <code>PutItem</code>\n            operation to succeed.</p>\n        <p>An expression can contain any of the following:</p>\n        <ul>\n            <li>\n                <p>Functions: <code>attribute_exists | attribute_not_exists | attribute_type |\n                        contains | begins_with | size</code>\n                </p>\n                <p>These function names are case-sensitive.</p>\n            </li>\n            <li>\n                <p>Comparison operators: <code>= | <> |\n            < | > | <= | >= |\n            BETWEEN | IN </code>\n                </p>\n            </li>\n            <li>\n                <p> Logical operators: <code>AND | OR | NOT</code>\n                </p>\n            </li>\n         </ul>\n        <p>For more information on condition expressions, see <a href=\"https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Expressions.SpecifyingConditions.html\">Condition Expressions</a> in the <i>Amazon DynamoDB Developer\n                Guide</i>.</p>")),
    ExpressionAttributeNameMap.underlyingSchema.optional[PutItemInput]("ExpressionAttributeNames", _.expressionAttributeNames).addHints(smithy.api.Documentation("<p>One or more substitution tokens for attribute names in an expression. The following\n            are some use cases for using <code>ExpressionAttributeNames</code>:</p>\n        <ul>\n            <li>\n                <p>To access an attribute whose name conflicts with a DynamoDB reserved\n                    word.</p>\n            </li>\n            <li>\n                <p>To create a placeholder for repeating occurrences of an attribute name in an\n                    expression.</p>\n            </li>\n            <li>\n                <p>To prevent special characters in an attribute name from being misinterpreted\n                    in an expression.</p>\n            </li>\n         </ul>\n        <p>Use the <b>#</b> character in an expression to dereference\n            an attribute name. For example, consider the following attribute name:</p>\n        <ul>\n            <li>\n                <p>\n                    <code>Percentile</code>\n                </p>\n            </li>\n         </ul>\n        <p>The name of this attribute conflicts with a reserved word, so it cannot be used\n            directly in an expression. (For the complete list of reserved words, see <a href=\"https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/ReservedWords.html\">Reserved Words</a> in the <i>Amazon DynamoDB Developer\n            Guide</i>). To work around this, you could specify the following for\n                <code>ExpressionAttributeNames</code>:</p>\n        <ul>\n            <li>\n                <p>\n                    <code>{\"#P\":\"Percentile\"}</code>\n                </p>\n            </li>\n         </ul>\n        <p>You could then use this substitution in an expression, as in this example:</p>\n        <ul>\n            <li>\n                <p>\n                    <code>#P = :val</code>\n                </p>\n            </li>\n         </ul>\n        <note>\n            <p>Tokens that begin with the <b>:</b> character are\n                    <i>expression attribute values</i>, which are placeholders for the\n                actual value at runtime.</p>\n        </note>\n        <p>For more information on expression attribute names, see <a href=\"https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Expressions.AccessingItemAttributes.html\">Specifying Item Attributes</a> in the <i>Amazon DynamoDB Developer\n                Guide</i>.</p>")),
    ExpressionAttributeValueMap.underlyingSchema.optional[PutItemInput]("ExpressionAttributeValues", _.expressionAttributeValues).addHints(smithy.api.Documentation("<p>One or more values that can be substituted in an expression.</p>\n        <p>Use the <b>:</b> (colon) character in an expression to\n            dereference an attribute value. For example, suppose that you wanted to check whether\n            the value of the <i>ProductStatus</i> attribute was one of the following: </p>\n        <p>\n            <code>Available | Backordered | Discontinued</code>\n        </p>\n        <p>You would first need to specify <code>ExpressionAttributeValues</code> as\n            follows:</p>\n        <p>\n            <code>{ \":avail\":{\"S\":\"Available\"}, \":back\":{\"S\":\"Backordered\"},\n                \":disc\":{\"S\":\"Discontinued\"} }</code>\n        </p>\n        <p>You could then use these values in an expression, such as this:</p>\n        <p>\n            <code>ProductStatus IN (:avail, :back, :disc)</code>\n        </p>\n        <p>For more information on expression attribute values, see <a href=\"https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Expressions.SpecifyingConditions.html\">Condition Expressions</a> in the <i>Amazon DynamoDB Developer\n                Guide</i>.</p>")),
  ){
    PutItemInput.apply
  }.withId(id).addHints(hints)
}