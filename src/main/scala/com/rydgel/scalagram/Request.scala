package com.rydgel.scalagram

import com.rydgel.scalagram.responses._
import dispatch._, Defaults._
import play.api.libs.json._


object Request {

  /**
   * Send the prepared request to an URL and parse the response to an appropriate case class.
   *
   * @param request Req, the dispatch request ready to by sent
   * @tparam T      represent the type of the Instagram data requested
   * @return        a Future of Response[T]
   */
  def send[T](request: Req)(implicit r: Reads[T]): Future[Response[T]] = {
    Http(request).map { resp =>
      val response = resp.getResponseBody
      val headers = resp.getHeaders
      if (resp.getStatusCode != 200) throw new Exception(parseMeta(response).toString)
      val data = (Json.parse(response) \ "data").validate[T] match {
        case JsError(e) => throw new Exception(e.toString())
        case JsSuccess(value, _) => value match {
          case None => None
          case _ => Some(value)
        }
      }
      val pagination = (Json.parse(response) \ "pagination").asOpt[Pagination]
      Response[T](data, pagination, parseMeta(response), headers)
    }
  }

  private def parseMeta(response: String): Meta = {
    val errorMeta = Meta(Some("UnknownException"), 500, Some("Unknown error"))
    (Json.parse(response) \ "meta").validate[Meta].getOrElse(errorMeta)
  }

}