package com.rydgel.scalagram.responses

import play.api.libs.json.Json

case class Image(url: String, width: Int, height: Int) extends InstagramData
object Image {
  implicit val ImageReads = Json.reads[Image]
  implicit val ImageWrites = Json.writes[Image]
}
