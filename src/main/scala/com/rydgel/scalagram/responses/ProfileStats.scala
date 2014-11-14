package com.rydgel.scalagram.responses

import play.api.libs.json._

case class ProfileStats(
    media: Int,
    follows: Int,
    followed_by: Int) {
  lazy val followedBy = followed_by
}
object ProfileStats {
  implicit val ProfileStatsReads = Json.reads[ProfileStats]
  implicit val ProfileStatsWrites = Json.writes[ProfileStats]
}