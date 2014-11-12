package com.rydgel.scalagram.responses

import play.api.libs.json._

case class Oauth(access_token: String, user: User)
object Oauth {
  implicit val OauthReads = Json.reads[Oauth]
  implicit val OauthWrites = Json.writes[Oauth]
}