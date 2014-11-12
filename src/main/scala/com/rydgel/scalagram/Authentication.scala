package com.rydgel.scalagram

import com.rydgel.scalagram.responses._
import dispatch._, Defaults._
import play.api.libs.json.Json

sealed trait Authentication
case class ClientId(id: String) extends Authentication
case class AccessToken(token: String) extends Authentication

object Authentication {

  /**
   * Transform an Authentication type to be used in a URL.
   *
   * @param a Authentication
   * @return  String
   */
  def toGETParams(a: Authentication): String = a match {
    case ClientId(id) => s"client_id=$id"
    case AccessToken(token) => s"access_token=$token"
  }

  /** Scope string which will be append to the URL.
    *
    * @param comments       Comments scope.
    * @param relationships  Relationships scope.
    * @param likes          Likes scope.
    * @return               String
    */
  def scopes(comments: Boolean = false, relationships: Boolean = false, likes: Boolean = false): String = {
    val scopes = List(
      if (comments) "comments" else "",
      if (relationships) "relationships" else "",
      if (likes) "likes" else ""
    ).filter(_ != "")

    if (scopes.headOption.isDefined) scopes.mkString("scope=", "+", "")
    else ""
  }

  /** Create the URL to call when retrieving an authentication code.
    *
    * @param clientId       Client identifier. (You need to register this on instagram.com/developer)
    * @param redirectURI    URI which the response is sent to. (You need to register this on instagram.com/developer)
    * @param comments       Require comment scope.
    * @param relationships  Require relationships scope.
    * @param likes          Require likes scope.
    * @return               String URL.
    */
  def codeURL(clientId: String, redirectURI: String, comments: Boolean = false,relationships: Boolean = false, likes: Boolean = false): String = {
    s"https://api.instagram.com/oauth/authorize/?client_id=$clientId&redirect_uri=$redirectURI" +
    s"&response_type=code&${scopes(comments, relationships, likes)}"
  }

  /** Create the URL to call when retrieving an access token.
    *
    * @param clientId       Client identifier. (You need to register this on instagram.com/developer)
    * @param redirectURI    URI which the response is sent to. (You need to register this on instagram.com/developer)
    * @param comments       Require comment scope.
    * @param relationships  Require relationships scope.
    * @param likes          Require likes scope.
    * @return               String URL.
    */
  def tokenURL(clientId: String, redirectURI: String, comments: Boolean = false, relationships: Boolean = false,likes: Boolean = false): String = {
    s"https://api.instagram.com/oauth/authorize/?client_id=$clientId&redirect_uri=$redirectURI" +
    s"&response_type=token&${scopes(comments, relationships, likes)}"
  }

  /** Post request to exchange a authentication code against an access token.
    * Note that an authentication code is valid one time only.
    *
    * @param clientId     Client identifier. (You need to register this on instagram.com/developer)
    * @param clientSecret Client secret. (You need to register this on instagram.com/developer)
    * @param redirectURI  URI which the response is sent to. (You need to register this on instagram.com/developer)
    * @param code         Authentication code. You can retrieve it via codeURL.
    * @return             Either[Authentication, ScalagramError].
    */
  def requestToken(clientId: String, clientSecret: String, redirectURI: String, code: String): Either[Authentication, Response[InstagramError]] = {
    val args = Map("client_id" -> clientId, "client_secret" -> clientSecret, "redirect_uri" -> redirectURI, "code" -> code, "grant_type" -> "authorization_code")
    val request = url("https://api.instagram.com/oauth/access_token") << args
    val response = Http(request > as.String).apply()

    Json.parse(response).asOpt[Oauth].getOrElse {
      Json.parse(response).asOpt[Meta]
    } match {
      case o: Oauth => Left(AccessToken(o.access_token))
      case Some(e: Meta) => Right(Response[InstagramError](None, None, e))
      case _ => Right(Response[InstagramError](None, None, Meta(Some("OauthException"), 500, Some("Unknown error"))))
    }
  }

}
