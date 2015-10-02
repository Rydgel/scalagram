package com.rydgel.scalagram

import com.rydgel.scalagram.responses.{AccessToken, ClientId}
import org.scalatest._

class AuthenticationSpec extends FlatSpec with Matchers {

  "An Authentication" should "be transformed in GET params correctly" in {
    val authClientId = ClientId("67890kdhe")
    Authentication.toGETParams(authClientId) should be ("client_id=67890kdhe")
    val authToken = AccessToken("dat-access-token")
    Authentication.toGETParams(authToken) should be ("access_token=dat-access-token")
  }

  "A Scope" should "be generated correctly" in {
    Authentication.scopes(comments = false, relationships = false, likes = false) should be ("")
    Authentication.scopes(comments = true, relationships = false, likes = false) should be ("scope=comments")
    Authentication.scopes(comments = false, relationships = true, likes = false) should be ("scope=relationships")
    Authentication.scopes(comments = false, relationships = false, likes = true) should be ("scope=likes")
    Authentication.scopes(comments = true, relationships = true, likes = false) should be ("scope=comments+relationships")
    Authentication.scopes(comments = true, relationships = false, likes = true) should be ("scope=comments+likes")
    Authentication.scopes(comments = false, relationships = true, likes = true) should be ("scope=relationships+likes")
    Authentication.scopes(comments = true, relationships = true, likes = true) should be ("scope=comments+relationships+likes")
  }

  "A codeUrl" should "be generated correctly" in {
    val clientId = "my-client-id"
    val redirectURI = "http://www.google.fr"
    val codeUrl = Authentication.codeURL(clientId, redirectURI, comments = true, relationships = false, likes = false)
    codeUrl should be (
      s"https://api.instagram.com/oauth/authorize/?client_id=$clientId&redirect_uri=$redirectURI" +
      s"&response_type=code&scope=comments"
    )
  }

  "A tokenUrl" should "be generated correctly" in {
    val clientId = "my-client-id"
    val redirectURI = "http://www.google.fr"
    val codeUrl = Authentication.tokenURL(clientId, redirectURI, comments = true, relationships = false, likes = false)
    codeUrl should be (
      s"https://api.instagram.com/oauth/authorize/?client_id=$clientId&redirect_uri=$redirectURI" +
      s"&response_type=token&scope=comments"
    )
  }

  "createSignedParam" should "return a signed param for Instagram" in {
    val secret = "6dc1787668c64c939929c17683d7cb74"
    val endpoint = "/media/657988443280050001_25025320"
    val params = Map(
      "access_token" -> Some("fb2e77d.47a0479900504cb3ab4a1f626d174d2d"),
      "count" -> Some("10")
    )
    Authentication.createSignedParam(secret, endpoint, params) should be (
      "260634b241a6cfef5e4644c205fb30246ff637591142781b86e2075faf1b163a"
    )
  }

}
