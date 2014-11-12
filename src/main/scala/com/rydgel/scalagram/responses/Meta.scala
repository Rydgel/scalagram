package com.rydgel.scalagram.responses

import play.api.libs.json.Json

case class Meta(error_type: Option[String],
                code: Int,
                error_message: Option[String]) {
  lazy val errorType = error_type
  lazy val errorMessage = error_message
}
object Meta {
  implicit val MetaReads = Json.reads[Meta]
  implicit val MetaWrites = Json.writes[Meta]
}
