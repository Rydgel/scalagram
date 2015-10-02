package com.rydgel.scalagram

import com.ning.http.client.FluentCaseInsensitiveStringsMap
import com.rydgel.scalagram.responses._
import dispatch._, Defaults._
import play.api.libs.json.Json

import scala.collection.JavaConverters._
import java.net._
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Mac

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
    case SignedAccessToken(token, _) => s"access_token=$token"
  }

  /**
   * Tell if authentication type is secure.
   *
   * @param a Authentication
   * @return  Boolean
   */
  def isSecure(a: Authentication): Boolean = a match {
    case SignedAccessToken(_, _) => true
    case _ => false
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

    if (scopes.nonEmpty) scopes.mkString("scope=", "+", "")
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
   * Converts an array of `bytes` to a hexadecimal representation String.
   * "Stolen" from the sbt source code.
   * Credits: http://www.scala-sbt.org/0.13.7/sxr/sbt/Hash.scala.html
   *
   * @param bytes Array of bytes
   * @return      String
   */
  private def toHex(bytes: Array[Byte]): String = {
    val buffer = new StringBuilder(bytes.length * 2)
    for (i <- bytes.indices) {
      val b = bytes(i)
      val bi: Int = if (b < 0) b + 256 else b
      buffer append toHex((bi >>> 4).asInstanceOf[Byte])
      buffer append toHex((bi & 0x0F).asInstanceOf[Byte])
    }
    buffer.toString()
  }

  private def toHex(b: Byte): Char = {
    require(b >= 0 && b <= 15, "Byte " + b + " was not between 0 and 15")
    if (b < 10) ('0'.asInstanceOf[Int] + b).asInstanceOf[Char]
    else        ('a'.asInstanceOf[Int] + (b - 10)).asInstanceOf[Char]
  }

  /**
   * Signed header for Instagram. More informations on this page
   * http://instagram.com/developer/restrict-api-requests/
   *
   * @param clientSecret  Your Instagram client secret token.
   * @param endpoint      API endpoint
   * @param params        Map of API parameters
   * @return String       Encoded header.
   */
  def createSignedParam(clientSecret: String, endpoint: String, params: Map[String, Option[String]]): String = {
    val paramsString = params.keys.toList.sorted.map(key => s"|$key=${params(key).mkString}").mkString
    val sig = s"$endpoint$paramsString"
    val secret = new SecretKeySpec(clientSecret.getBytes, "HmacSHA256")
    val mac = Mac.getInstance("HmacSHA256")
    mac.init(secret)
    val result = mac.doFinal(sig.getBytes)
    toHex(result)
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
    val args = Map(
      "client_id" -> clientId, "client_secret" -> clientSecret, "redirect_uri" -> redirectURI,
      "code" -> code, "grant_type" -> "authorization_code"
    )
    val request = url("https://api.instagram.com/oauth/access_token") << args
    Http(request).map { resp =>
      val response = resp.getResponseBody
      val headers = ningHeadersToMap(resp.getHeaders)
      if (resp.getStatusCode != 200) throw new Exception(parseMeta(response).toString)
      Json.parse(response).asOpt[Oauth] match {
        case Some(o: Oauth) => Response(Some(AccessToken(o.accessToken)), None, Meta(None, 200, None), headers)
        case _ =>
          val errorMeta = Meta(Some("OauthException"), 500, Some("Unknown error"))
          val meta = Json.parse(response).asOpt[Meta].getOrElse(errorMeta)
          Response(None, None, meta, headers)
      }
    }
  }

  private def parseMeta(response: String): Meta = {
    val errorMeta = Meta(Some("OauthException"), 500, Some("Unknown error"))
    Json.parse(response).validate[Meta].getOrElse(errorMeta)
  }

  private def ningHeadersToMap(headers: FluentCaseInsensitiveStringsMap) = {
    mapAsScalaMapConverter(headers).asScala.map(e => e._1 -> e._2.asScala.toSeq).toMap
  }

}
