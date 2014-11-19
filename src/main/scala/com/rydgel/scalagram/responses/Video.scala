package com.rydgel.scalagram.responses

import play.api.libs.json.Json

case class Video(url: String, width: Int, height: Int) extends InstagramData
object Video {
  implicit val VideoReads = Json.reads[Video]
  implicit val VideoWrites = Json.writes[Video]
}
