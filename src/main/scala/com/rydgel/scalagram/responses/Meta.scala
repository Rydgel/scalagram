package com.rydgel.scalagram.responses

case class Meta(error_type: Option[String],
                code: Int,
                error_message: Option[String]) {
  lazy val errorType = error_type
  lazy val errorMessage = error_message
}
