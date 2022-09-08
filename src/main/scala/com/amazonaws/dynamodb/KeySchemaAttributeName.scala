package com.amazonaws.dynamodb

import smithy4s.Schema
import smithy4s.Hints
import smithy4s.schema.Schema.string
import smithy4s.ShapeId
import smithy4s.schema.Schema.bijection
import smithy4s.Newtype

object KeySchemaAttributeName extends Newtype[String] {
  val id: ShapeId = ShapeId("com.amazonaws.dynamodb", "KeySchemaAttributeName")
  val hints : Hints = Hints()
  val underlyingSchema : Schema[String] = string.withId(id).addHints(hints).validated(smithy.api.Length(min = Some(1), max = Some(255)))
  implicit val schema : Schema[KeySchemaAttributeName] = bijection(underlyingSchema, asBijection)
}