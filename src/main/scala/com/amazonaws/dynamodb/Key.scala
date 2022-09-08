package com.amazonaws.dynamodb

import smithy4s.Schema
import smithy4s.Hints
import smithy4s.ShapeId
import smithy4s.schema.Schema.map
import smithy4s.schema.Schema.bijection
import smithy4s.Newtype

object Key extends Newtype[Map[AttributeName,AttributeValue]] {
  val id: ShapeId = ShapeId("com.amazonaws.dynamodb", "Key")
  val hints : Hints = Hints.empty
  val underlyingSchema : Schema[Map[AttributeName,AttributeValue]] = map(AttributeName.schema, AttributeValue.schema).withId(id).addHints(hints)
  implicit val schema : Schema[Key] = bijection(underlyingSchema, asBijection)
}