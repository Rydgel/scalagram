package com.rydgel.scalagram.responses

import play.api.libs.json._

case class User(id: String, username: String, full_name: String, profile_picture: String) extends InstagramData {
  lazy val fullName = full_name
  lazy val profilePicture = profile_picture
}
object User {
  implicit val UserReads = Json.reads[User]
  implicit val UserWrites = Json.writes[User]
}
