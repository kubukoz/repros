package com.amazonaws.dynamodb

import smithy4s.Schema
import smithy4s.schema.Schema.list
import smithy4s.Hints
import smithy4s.ShapeId
import smithy4s.schema.Schema.bijection
import smithy4s.Newtype

object ContributorInsightsSummaries extends Newtype[List[ContributorInsightsSummary]] {
  val id: ShapeId = ShapeId("com.amazonaws.dynamodb", "ContributorInsightsSummaries")
  val hints : Hints = Hints.empty
  val underlyingSchema : Schema[List[ContributorInsightsSummary]] = list(ContributorInsightsSummary.schema).withId(id).addHints(hints)
  implicit val schema : Schema[ContributorInsightsSummaries] = bijection(underlyingSchema, asBijection)
}