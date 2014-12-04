package com.rydgel.scalagram

import com.rydgel.scalagram.responses._
import org.scalatest._
import org.scalatest.matchers.{BePropertyMatchResult, BePropertyMatcher}

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.io.Source
import scala.util.Try

class ScalagramSpec extends FlatSpec with Matchers {

  private def anInstanceOf[T](implicit tag : reflect.ClassTag[T]) = {
    val clazz = tag.runtimeClass.asInstanceOf[Class[T]]
    new BePropertyMatcher[AnyRef] { def apply(left: AnyRef) =
      BePropertyMatchResult(left.getClass.isAssignableFrom(clazz), "an instance of " + clazz.getName) }
  }

  private def readAccessTokenFromConfig(): AccessToken = {
    Try {
      val tokenFile = Source.fromURL(getClass.getResource("/token.txt")).mkString
      AccessToken(tokenFile.split("=").toList(1))
    }.getOrElse(throw new Exception(
      "Please provide a valid access_token by making a token.txt in resources.\n" +
      "See token.txt.default for detail."
    ))
  }

  val auth = readAccessTokenFromConfig()
  val wrongToken = AccessToken("this is a bullshit access token")

  "A failed request" should "return a failed promise" in {
    an [Exception] should be thrownBy Await.result(Scalagram.userInfo(wrongToken, "36783"), 10 seconds)
  }

  "userInfo" should "return a Response[Profile]" in {
    val request = Await.result(Scalagram.userInfo(auth, "36783"), 10 seconds)
    request should be (anInstanceOf[Response[Profile]])
    request.data.get.id should be ("36783")
  }

  "userFeed" should "return a Response[List[Media]]" in {
    val request = Await.result(Scalagram.userFeed(auth), 10 seconds)
    request should be (anInstanceOf[Response[List[Media]]])
  }

  "mediaRecent" should "return a Response[List[Media]]" in {
    val request = Await.result(Scalagram.mediaRecent(auth, "36783"), 10 seconds)
    request should be (anInstanceOf[Response[List[Media]]])
  }

  "liked" should "return a Response[List[Media]]" in {
    val request = Await.result(Scalagram.liked(auth), 10 seconds)
    request should be (anInstanceOf[Response[List[Media]]])
  }

  "userSearch" should "return a Response[List[UserSearch]]" in {
    val request = Await.result(Scalagram.userSearch(auth, "rydgel"), 10 seconds)
    request should be (anInstanceOf[Response[List[UserSearch]]])
  }

  "follows" should "return a Response[List[User]]" in {
    val request = Await.result(Scalagram.follows(auth, "36783"), 10 seconds)
    request should be (anInstanceOf[Response[List[User]]])
  }

  "followedBy" should "return a Response[List[User]]" in {
    val request = Await.result(Scalagram.followedBy(auth, "36783"), 10 seconds)
    request should be (anInstanceOf[Response[List[User]]])
  }

  "relationshipRequests" should "return a Response[List[User]]" in {
    val request = Await.result(Scalagram.relationshipRequests(auth), 10 seconds)
    request should be (anInstanceOf[Response[List[User]]])
  }

  "relationship" should "return a Response[Relationship]" in {
    val request = Await.result(Scalagram.relationship(auth, "36783"), 10 seconds)
    request should be (anInstanceOf[Response[Relationship]])
  }

  "media" should "return a Response[Media]" in {
    val request = Await.result(Scalagram.media(auth, "867431466615431943_36783"), 10 seconds)
    request should be (anInstanceOf[Response[Media]])
  }

  "mediaShortcode" should "return a Response[Media]" in {
    val request = Await.result(Scalagram.mediaShortcode(auth, "wJvFKym-MH"), 10 seconds)
    request should be (anInstanceOf[Response[Media]])
  }

  "mediaSearch" should "return a Response[List[Media]]" in {
    val request = Await.result(Scalagram.mediaSearch(auth, (48.858844, 2.294351)), 10 seconds)
    request should be (anInstanceOf[Response[List[Media]]])
  }

  "popular" should "return a Response[List[Media]]" in {
    val request = Await.result(Scalagram.popular(auth), 10 seconds)
    request should be (anInstanceOf[Response[List[Media]]])
  }

  "comments" should "return a Response[List[Comment]]" in {
    val request = Await.result(Scalagram.comments(auth, "795030688240492786_36783"), 10 seconds)
    request should be (anInstanceOf[Response[List[Comment]]])
  }

  "likes" should "return a Response[List[User]]" in {
    val request = Await.result(Scalagram.likes(auth, "795030688240492786_36783"), 10 seconds)
    request should be (anInstanceOf[Response[List[User]]])
  }

  "tagInformation" should "return a Response[Tag]" in {
    val request = Await.result(Scalagram.tagInformation(auth, "vscocam"), 10 seconds)
    request should be (anInstanceOf[Response[responses.Tag]])
  }

  "tagRecent" should "return a Response[List[Media]]" in {
    val request = Await.result(Scalagram.tagRecent(auth, "vscocam"), 10 seconds)
    request should be (anInstanceOf[Response[List[Media]]])
  }

  "tagSearch" should "return a Response[List[Tag]]" in {
    val request = Await.result(Scalagram.tagSearch(auth, "vscocam"), 10 seconds)
    request should be (anInstanceOf[Response[List[responses.Tag]]])
  }

  "location" should "return a Response[LocationSearch]" in {
    val request = Await.result(Scalagram.location(auth, "1"), 10 seconds)
    request should be (anInstanceOf[Response[LocationSearch]])
  }

  "locationMedia" should "return a Response[List[Media]]" in {
    val request = Await.result(Scalagram.locationMedia(auth, "482406435"), 10 seconds)
    request should be (anInstanceOf[Response[List[Media]]])
  }

  "locationSearch" should "return a Response[List[LocationSearch]]" in {
    val coordinates = (35.944704325, -78.951736232)
    val request = Await.result(Scalagram.locationSearch(auth, Some(coordinates)), 10 seconds)
    request should be (anInstanceOf[Response[List[LocationSearch]]])
    println(request)
  }

}
