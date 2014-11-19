package com.rydgel.scalagram.responses

import play.api.libs.json.Json

case class Likes(count: Int, data: List[User]) {
  lazy val users = data
}
object Likes {
  implicit val LikesReads = Json.reads[Likes]
  implicit val LikesWrites = Json.writes[Likes]
}
