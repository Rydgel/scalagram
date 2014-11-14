package com.rydgel.scalagram

import com.rydgel.scalagram.responses._
import dispatch._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Success
import scala.concurrent.ExecutionContext.Implicits.global

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
    /*val at = AccessToken("36783.d949468.13d14f697ab5496188bb2e34e8f46acc")
    val future = userInfo(at, "36783")

    future onSuccess {
      case ResponseOK(data, pagination, meta) => println(data)
      case ResponseError(meta) => println(meta)
    }

    Thread.sleep(10000)*/
  }

}
