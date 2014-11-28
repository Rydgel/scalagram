package com.rydgel.scalagram.responses

import play.api.libs.json._

case class Relationship(outgoing_status: Option[String], incoming_status: Option[String]) {
  lazy val outgoingStatus = outgoing_status
  lazy val incomingStatus = incoming_status
}
object Relationship {
  implicit val RelationshipReads = Json.reads[Relationship]
  implicit val RelationshipWrites = Json.writes[Relationship]
}
