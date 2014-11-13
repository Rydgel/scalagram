package com.rydgel.scalagram

import com.rydgel.scalagram.responses._
import dispatch._, Defaults._
import play.api.libs.json.{Reads, JsUndefined, Json}

import scala.util.Try

object Request {

  /**
   *
   * @param request Req, the dispatch request ready to by sent
   * @tparam T represent the type of the Instagram data requested
   * @return a Future of Response[T]
   */
  def send[T <: InstagramData](request: Req)(implicit r: Reads[T]): Future[Response[T]] = {
    val responseFuture = Http(request > as.String)
    responseFuture.map { response =>
      tryRequest[T](response) match {
        case ParseError(e) => ResponseError(e)
        case ParseOk(t) => ResponseOK[T](t, tryPagination(response), Meta(None, 200, None))
      }
    }
  }

  private def tryRequest[T <: InstagramData](response: String)(implicit r: Reads[T]): Parse[T] = {
    Try(
      Json.parse(response) \ "data" match {
        case j: JsUndefined => tryMeta(response)
        case x => x.asOpt[T]
      }).toOption.flatten match {
        case None => ParseError(Meta(Some("OauthException"), 500, Some("Unknown error")))
        case Some(m: Meta) => ParseError(m)
        case Some(t: InstagramData) => ParseOk(t)
        case _ => ParseError(Meta(Some("OauthException"), 500, Some("Unknown error")))

    }
  }

  private def tryMeta(response: String): Option[Meta] = {
    Try(
      Json.parse(response) \ "meta" match {
        case j: JsUndefined => None
        case x => x.asOpt[Meta]
      }
    ).toOption.flatten
  }

  private def tryPagination(response: String): Option[Pagination] = {
    Try(
      Json.parse(response) \ "pagination" match {
        case j: JsUndefined => None
        case x => x.asOpt[Pagination]
      }
    ).toOption.flatten
  }

}
