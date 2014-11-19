package com.rydgel.scalagram

import com.rydgel.scalagram.responses._
import dispatch._, Defaults._
import play.api.libs.json.Json


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

  /**
   * Scope string which will be append to the URL.
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

  /**
   * Create the URL to call when retrieving an authentication code.
   *
   * @param clientId       Client identifier. (You need to register this on instagram.com/developer)
   * @param redirectURI    URI which the response is sent to. (You need to register this on instagram.com/developer)
   * @param comments       Require comment scope.
   * @param relationships  Require relationships scope.
   * @param likes          Require likes scope.
   * @return               String URL.
   */
  def codeURL(clientId: String, redirectURI: String, comments: Boolean = false, relationships: Boolean = false, likes: Boolean = false): String = {
    s"https://api.instagram.com/oauth/authorize/?client_id=$clientId&redirect_uri=$redirectURI" +
    s"&response_type=code&${scopes(comments, relationships, likes)}"
  }

  /**
   * Create the URL to call when retrieving an access token.
   *
   * @param clientId       Client identifier. (You need to register this on instagram.com/developer)
   * @param redirectURI    URI which the response is sent to. (You need to register this on instagram.com/developer)
   * @param comments       Require comment scope.
   * @param relationships  Require relationships scope.
   * @param likes          Require likes scope.
   * @return               String URL.
   */
  def tokenURL(clientId: String, redirectURI: String, comments: Boolean = false, relationships: Boolean = false, likes: Boolean = false): String = {
    s"https://api.instagram.com/oauth/authorize/?client_id=$clientId&redirect_uri=$redirectURI" +
    s"&response_type=token&${scopes(comments, relationships, likes)}"
  }

  /**
   * Post request to exchange a authentication code against an access token.
   * Note that an authentication code is valid one time only.
   *
   * @param clientId     Client identifier. (You need to register this on instagram.com/developer)
   * @param clientSecret Client secret. (You need to register this on instagram.com/developer)
   * @param redirectURI  URI which the response is sent to. (You need to register this on instagram.com/developer)
   * @param code         Authentication code. You can retrieve it via codeURL.
   * @return             Future of Response[Authentication]
   */
  def requestToken(clientId: String, clientSecret: String, redirectURI: String, code: String): Future[Response[Authentication]] = {
    val args = Map("client_id" -> clientId, "client_secret" -> clientSecret, "redirect_uri" -> redirectURI, "code" -> code, "grant_type" -> "authorization_code")
    val request = url("https://api.instagram.com/oauth/access_token") << args
    val responseFuture = Http(request)

    responseFuture.map { resp =>
      val response = resp.getResponseBody
      if (resp.getStatusCode != 200) throw new Exception(parseMeta(response).toString)
      Json.parse(response).asOpt[Oauth] match {
        case Some(o: Oauth) => Response(Some(AccessToken(o.accessToken)), None, Meta(None, 200, None))
        case _ =>
          val errorMeta = Meta(Some("OauthException"), 500, Some("Unknown error"))
          val meta = Json.parse(response).asOpt[Meta].getOrElse(errorMeta)
          Response(None, None, meta)
      }
    }
  }

  private def parseMeta(response: String): Meta = {
    val errorMeta = Meta(Some("OauthException"), 500, Some("Unknown error"))
    Json.parse(response).validate[Meta].getOrElse(errorMeta)
  }

}
