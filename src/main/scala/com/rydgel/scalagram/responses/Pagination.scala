package com.rydgel.scalagram.responses

import play.api.libs.json._

case class Pagination(
    next_url: Option[String],
    next_max_id: Option[String],
    next_min_id: Option[String],
    next_max_like_id: Option[String],
    next_cursor: Option[String],
    deprecation_warning: Option[String],
    next_max_tag_id: Option[String],
    next_min_tag_id: Option[String],
    min_tag_id: Option[String],
    max_tag_id: Option[String]) {
  lazy val nextURL = next_url
  lazy val nextMaxId = next_max_id
  lazy val nextMinId = next_min_id
  lazy val nextMaxLikeId = next_max_like_id
  lazy val nextCursor = next_cursor
  lazy val nextMaxTagId = next_max_tag_id
  lazy val nextMinTagId = next_min_tag_id
  lazy val minTagId = min_tag_id
  lazy val maxTagId = max_tag_id
}
object Pagination {
  implicit val PaginationReads = Json.reads[Pagination]
  implicit val PaginationWrites = Json.writes[Pagination]
}
