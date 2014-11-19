package com.rydgel.scalagram.responses

import play.api.libs.json._

case class UserSearch (
    username: String,
    bio: Option[String],
    website: Option[String],
    profile_picture: String,
    full_name: String,
    id: String) {
  lazy val fullName = full_name
  lazy val profilePicture = profile_picture
}

object UserSearch {
  implicit val UserSearchReads = Json.reads[UserSearch]
  implicit val UserSearchWrites = Json.writes[UserSearch]
}
