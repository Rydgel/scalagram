package com.rydgel.scalagram.responses

import play.api.libs.json._

case class Comments(data: List[Comment], count: Int) extends InstagramData
object Comments {
  implicit val CommentsReads = Json.reads[Comments]
  implicit val CommentsWrites = Json.writes[Comments]
}
