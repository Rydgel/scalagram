package com.rydgel.scalagram.responses

sealed trait Authentication
case class ClientId(id: String) extends Authentication
case class AccessToken(token: String) extends Authentication
