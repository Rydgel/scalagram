package com.rydgel.scalagram.responses

import play.api.libs.json._

case class Images(low_resolution: Image, thumbnail: Image, standard_resolution: Image) {
  lazy val lowResolution = low_resolution
  lazy val standardResolution = standard_resolution
}
object Images {
  implicit val ImagesReads = Json.reads[Images]
  implicit val ImagesWrites = Json.writes[Images]
}
