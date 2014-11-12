package com.rydgel.scalagram.responses

import play.api.libs.json._

case class ScalagramError(code: Int, error_type: String, error_message: String) {
  lazy val errorType = error_type
  lazy val errorMessage = error_message
}
object ScalagramError {
  implicit val ScalagramErrorReads = Json.reads[ScalagramError]
  implicit val ScalagramErrorWrites = Json.writes[ScalagramError]
}
