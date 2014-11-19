package com.rydgel.scalagram.responses

import play.api.libs.json._

case class Comment(created_time: String, text: String, from: User, id: String) extends InstagramData {
  lazy val createdTime = created_time
}
object Comment {
  implicit val CommentReads = Json.reads[Comment]
  implicit val CommentWrites = Json.writes[Comment]
}
