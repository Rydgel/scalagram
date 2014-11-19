package com.rydgel.scalagram.responses

import play.api.libs.json._

case class UserInPhoto(user: User, position: Position) extends InstagramData
object UserInPhoto {
  implicit val UserInPhotoReads = Json.reads[UserInPhoto]
  implicit val UserInPhotoWrites = Json.writes[UserInPhoto]
}
