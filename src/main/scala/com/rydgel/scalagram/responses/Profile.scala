package com.rydgel.scalagram.responses

import play.api.libs.json._

case class Profile(id: String,
                   username: String,
                   full_name: Option[String],
                   profile_picture: Option[String],
                   bio: Option[String],
                   website: Option[String],
                   counts: ProfileStats) {
  lazy val fullName = full_name
  lazy val profilePicture = profile_picture
}
object Profile {
  implicit val ProfileReads = Json.reads[Profile]
  implicit val ProfileWrites = Json.writes[Profile]
}
