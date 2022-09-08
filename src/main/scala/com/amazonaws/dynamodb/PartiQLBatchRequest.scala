package com.amazonaws.dynamodb

import smithy4s.Schema
import smithy4s.schema.Schema.list
import smithy4s.Hints
import smithy4s.ShapeId
import smithy4s.schema.Schema.bijection
import smithy4s.Newtype

object PartiQLBatchRequest extends Newtype[List[BatchStatementRequest]] {
  val id: ShapeId = ShapeId("com.amazonaws.dynamodb", "PartiQLBatchRequest")
  val hints : Hints = Hints()
  val underlyingSchema : Schema[List[BatchStatementRequest]] = list(BatchStatementRequest.schema).withId(id).addHints(hints).validated(smithy.api.Length(min = Some(1), max = Some(25)))
  implicit val schema : Schema[PartiQLBatchRequest] = bijection(underlyingSchema, asBijection)
}