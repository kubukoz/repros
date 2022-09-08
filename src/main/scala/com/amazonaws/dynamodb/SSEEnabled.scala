package com.amazonaws.dynamodb

import smithy4s.Schema
import smithy4s.Hints
import smithy4s.schema.Schema.boolean
import smithy4s.ShapeId
import smithy4s.schema.Schema.bijection
import smithy4s.Newtype

object SSEEnabled extends Newtype[Boolean] {
  val id: ShapeId = ShapeId("com.amazonaws.dynamodb", "SSEEnabled")
  val hints : Hints = Hints(
    smithy.api.Box(),
  )
  val underlyingSchema : Schema[Boolean] = boolean.withId(id).addHints(hints)
  implicit val schema : Schema[SSEEnabled] = bijection(underlyingSchema, asBijection)
}