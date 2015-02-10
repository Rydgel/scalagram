package com.rydgel.scalagram.responses

import play.api.libs.json._

case class Media(
    attribution: Option[Attribution],
    `type`: String,
    users_in_photo: List[UserInPhoto],
    filter: String,
    tags: List[String],
    comments: Comments,
    caption: Option[Comment],
    likes: Likes,
    link: Option[String],
    user: UserSearch,
    created_time: String,
    images: Images,
    videos: Option[Videos],
    id: String,
    user_has_liked: Option[Boolean],
    location: Option[Location]) {
  lazy val usersInPhoto = users_in_photo
  lazy val createdTime = created_time
  lazy val userHasLiked = user_has_liked
}
object Media {
  implicit val MediaReads = Json.reads[Media]
  implicit val MediaWrites = Json.writes[Media]
}
