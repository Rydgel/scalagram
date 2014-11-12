package com.rydgel.scalagram

import com.rydgel.scalagram.responses._
import dispatch._

object Scalagram {

  private val urlInstagramRoot = "https://api.instagram.com/v1"

  def userInfo(auth: Authentication, userId: String): Response[Profile] = {
    val stringAuth = Authentication.toGETParams(auth)
    Request.send[Profile](url(s"$urlInstagramRoot/users/$userId/?$stringAuth"))
  }

  def main(array: Array[String]) = {
    println("Hello")
  }

}
