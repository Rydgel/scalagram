package com.rydgel.scalagram.responses

import play.api.libs.json.Json

case class Tag(media_count: Long, name: String) {
  lazy val mediaCount = media_count
}
object Tag {
  implicit val TagReads = Json.reads[Tag]
  implicit val TagWrites = Json.writes[Tag]
}
