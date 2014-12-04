package com.rydgel.scalagram.responses

import play.api.libs.json.Json

case class Location(id: Option[Int], latitude: Option[Double], longitude: Option[Double], name: Option[String])
object Location {
  implicit val LocationReads = Json.reads[Location]
  implicit val LocationWrites = Json.writes[Location]
}
