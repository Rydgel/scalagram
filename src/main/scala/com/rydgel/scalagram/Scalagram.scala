package com.rydgel.scalagram

import com.rydgel.scalagram.responses._
import dispatch._
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global

object Scalagram {

  private val urlInstagramRoot = "https://api.instagram.com/v1"

  /**
   * Get basic information about a user.
   *
   * @param auth   Credentials
   * @param userId Id-number of the name to get information about.
   * @return       A Future of Response[Profile]
   */
  def userInfo(auth: Authentication, userId: String): Future[Response[Profile]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val request = url(s"$urlInstagramRoot/users/$userId/?$stringAuth")
    Request.send[Profile](request)
  }

  /**
   * See the authenticated user's feed.
   *
   * @param auth  Credentials
   * @param count Max number of results to return.
   * @param minId Return media later than this.
   * @param maxId Return media earlier than this.
   * @return      A Future of a Response of a List[Media]
   */
  def userFeed(auth: Authentication, count: Option[Int] = None, minId: Option[String] = None,
               maxId: Option[String] = None): Future[Response[List[Media]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val request = url(s"$urlInstagramRoot/users/self/feed?$stringAuth&count=${count.mkString}" +
                      s"&min_id=${minId.mkString}&max_id=${maxId.mkString}")
    Request.send[List[Media]](request)
  }

  def main(array: Array[String]) = {
    val at = AccessToken("")
    val future = userFeed(at)

    future onSuccess {
      case Response(data, pagination, meta) => println(data) ; println(pagination)
    }

    future onFailure {
      case t => println("An error has occured: " + t.getMessage)
    }


    Thread.sleep(10000)
    /*val lines = scala.io.Source.fromFile("/tmp/json.json").mkString
    val js = Json.parse(lines)
    println(js.validate[List[Media]])*/
  }

}
