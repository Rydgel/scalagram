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
   * @return
   */
  def send[T <: InstagramData](request: Req)(implicit r: Reads[T]): Either[Response[T], Response[InstagramError]] = {
    val response = Http(request > as.String).apply()
    tryRequested[T](response) match {
      case e: Meta => Right(Response[InstagramError](None, None, e))
      case t: T => Left(Response(Some(t), tryPagination(response), Meta(None, 200, None)))
    }
  }

  private def tryRequested[T <: InstagramData](response: String)(implicit r: Reads[T]): InstagramData = {
    Try(
      (Json.parse(response) \ "data").asOpt[T].getOrElse { (Json.parse(response) \ "meta").as[Meta] }
    ).toOption.getOrElse(Meta(Some("OauthException"), 500, Some("Unknown error")))
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
