package com.rydgel.scalagram.responses

import play.api.libs.json._

case class Oauth(access_token: String, user: User) {
  lazy val accessToken = access_token
}
object Oauth {
  implicit val OauthReads = Json.reads[Oauth]
  implicit val OauthWrites = Json.writes[Oauth]
}