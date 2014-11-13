package com.rydgel.scalagram

import com.rydgel.scalagram.responses._
import dispatch._

object Scalagram {

  private val urlInstagramRoot = "https://api.instagram.com/v1"

  /**
   * Get basic information about a user.
   *
   * @param auth Credentials
   * @param userId Id-number of the name to get information about.
   * @return a Future of Response[Profile]
   */
  def userInfo(auth: Authentication, userId: String): Future[Response[Profile]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val request = url(s"$urlInstagramRoot/users/$userId/?$stringAuth")
    Request.send[Profile](request)
  }

  def main(array: Array[String]) = {
    println("Hello")
  }

}
