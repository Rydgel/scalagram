package com.rydgel.scalagram

import com.rydgel.scalagram.responses._
import dispatch._, Defaults._
import play.api.libs.json.{Reads, JsUndefined, Json}

import scala.util.Try

object Request {

  /**
   * Send the prepared request to an URL and parse the response to an appropriate case class.
   *
   * @param request Req, the dispatch request ready to by sent
   * @tparam T      represent the type of the Instagram data requested
   * @return        a Future of Response[T]
   */
  def send[T](request: Req)(implicit r: Reads[T]): Future[Response[T]] = {
    val responseFuture = Http(request > as.String)
    responseFuture.map { response =>
      tryData[T](response) match {
        case ParseError(e) => ResponseError(e)
        case ParseOk(t) => ResponseOK[T](Some(t), tryPagination(response), Meta(None, 200, None))
      }
    }
  }

  /**
   * Trying to parse the "data" field of a response.
   *
   * @param response String, Instagram response
   * @param r        JSON implicit reads for decoding
   * @tparam T       Whatever InstagramData you want to decode
   * @return         Parse[T] (ParseOk|ParseError)
   */
  private def tryData[T](response: String)(implicit r: Reads[T]): Parse[T] = {
    Try(
      Json.parse(response) \ "data" match {
        case j: JsUndefined => tryMeta(response)
        case x => x.asOpt[T]
      }).toOption.flatten match {
      case Some(m: Meta) => ParseError(m)
      case Some(t: T) => ParseOk(t)
      case _ => ParseError(Meta(Some("OauthException"), 500, Some("Unknown error")))
    }
  }

  /**
   * Trying to decode the "meta" field of a response.
   *
   * @param response String, Instagram response
   * @return         Option[Meta]
   */
  private def tryMeta(response: String): Option[Meta] = {
    Try(
      Json.parse(response) \ "meta" match {
        case j: JsUndefined => None
        case x => x.asOpt[Meta]
      }
    ).toOption.flatten
  }

  /**
   * Trying to decode the "pagination" field of a response.
   *
   * @param response String, Instagram response
   * @return         Option[Pagination]
   */
  private def tryPagination(response: String): Option[Pagination] = {
    Try(
      Json.parse(response) \ "pagination" match {
        case j: JsUndefined => None
        case x => x.asOpt[Pagination]
      }
    ).toOption.flatten
  }

}