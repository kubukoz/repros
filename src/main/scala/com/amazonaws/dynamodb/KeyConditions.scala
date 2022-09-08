package com.amazonaws.dynamodb

import smithy4s.Schema
import smithy4s.Hints
import smithy4s.ShapeId
import smithy4s.schema.Schema.map
import smithy4s.schema.Schema.bijection
import smithy4s.Newtype

object KeyConditions extends Newtype[Map[AttributeName,Condition]] {
  val id: ShapeId = ShapeId("com.amazonaws.dynamodb", "KeyConditions")
  val hints : Hints = Hints.empty
  val underlyingSchema : Schema[Map[AttributeName,Condition]] = map(AttributeName.schema, Condition.schema).withId(id).addHints(hints)
  implicit val schema : Schema[KeyConditions] = bijection(underlyingSchema, asBijection)
}