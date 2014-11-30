package com.rydgel.scalagram.responses

import play.api.libs.json._

case class Attribution(name: String, website: Option[String], itunes_url: Option[String]) {
  lazy val itunesUrl = itunes_url
}
object Attribution {
  implicit val AttributionReads = Json.reads[Attribution]
  implicit val AttributionWrites = Json.writes[Attribution]
}
