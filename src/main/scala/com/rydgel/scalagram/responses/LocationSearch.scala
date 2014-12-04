package com.rydgel.scalagram.responses

import play.api.libs.json.Json

/** Cause Instagram is returning a String for the Id in some endpoint... */
case class LocationSearch(id: Option[String], latitude: Option[Double], longitude: Option[Double], name: Option[String])
object LocationSearch {
  implicit val LocationSearchReads = Json.reads[LocationSearch]
  implicit val LocationSearchWrites = Json.writes[LocationSearch]
}
