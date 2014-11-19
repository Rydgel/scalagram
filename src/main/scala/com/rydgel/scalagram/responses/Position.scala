package com.rydgel.scalagram.responses

import play.api.libs.json._

case class Position(x: Float, y: Float) extends InstagramData
object Position {
  implicit val PositionReads = Json.reads[Position]
  implicit val PositionWrites = Json.writes[Position]
}
