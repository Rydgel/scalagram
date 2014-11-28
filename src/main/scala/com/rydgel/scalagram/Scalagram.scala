package com.rydgel.scalagram

import com.rydgel.scalagram.responses._
import dispatch._

object Scalagram {

  /**
   * Get basic information about a user.
   *
   * @param auth   Credentials.
   * @param userId Id-number of the name to get information about.
   * @return       A Future of Response[Profile].
   */
  def userInfo(auth: Authentication, userId: String): Future[Response[Profile]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val request = url(s"https://api.instagram.com/v1/users/$userId/?$stringAuth")
    Request.send[Profile](request)
  }

  /**
   * See the authenticated user's feed.
   *
   * @param auth  Credentials.
   * @param count Max number of results to return.
   * @param minId Return media later than this.
   * @param maxId Return media earlier than this.
   * @return      A Future of a Response of a List[Media].
   */
  def userFeed(auth: Authentication, count: Option[Int] = None, minId: Option[String] = None,
               maxId: Option[String] = None): Future[Response[List[Media]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val request = url(
      s"https://api.instagram.com/v1/users/self/feed?$stringAuth&count=${count.mkString}" +
      s"&min_id=${minId.mkString}&max_id=${maxId.mkString}"
    )
    Request.send[List[Media]](request)
  }

  /**
   * Get the most recent media published by a user.
   *
   * @param auth         Credentials.
   * @param userId       The Instagram user ID.
   * @param count        Max number of results to return.
   * @param minTimestamp Return media after this UNIX timestamp.
   * @param maxTimestamp Return media before this UNIX timestamp.
   * @param minId        Return media later than this.
   * @param maxId        Return media earlier than this.
   * @return             A Future of a Response of a List[Media].
   */
  def mediaRecent(auth: Authentication, userId: String, count: Option[Int] = None, minTimestamp: Option[String] = None,
                  maxTimestamp: Option[String] = None, minId: Option[String] = None, maxId: Option[String] = None
                 ): Future[Response[List[Media]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val request = url(
      s"https://api.instagram.com/v1/users/$userId/media/recent/?$stringAuth&max_timestamp=${maxTimestamp.mkString}" +
      s"&min_timestamp=${minTimestamp.mkString}&min_id=${minId.mkString}&max_id=${maxId.mkString}"
    )
    Request.send[List[Media]](request)
  }

  /**
   * See the authenticated user's list of liked media.
   *
   * @param auth      Credentials.
   * @param count     Max number of results to return.
   * @param maxLikeId Return media liked before this id.
   * @return          A Future of a Response of a List[Media].
   */
  def liked(auth: Authentication, count: Option[Int] = None, maxLikeId: Option[String] = None): Future[Response[List[Media]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val request = url(
      s"https://api.instagram.com/v1/users/self/media/liked?$stringAuth&count=${count.mkString}&max_like_id=${maxLikeId.mkString}"
    )
    Request.send[List[Media]](request)
  }

  /**
   * Search for a user by name.
   *
   * @param auth  Credentials.
   * @param name  Name to search a user for.
   * @param count Max number of results to return.
   * @return      A Future of a Response of a List[UserSearch].
   */
  def userSearch(auth: Authentication, name: String, count: Option[Int] = None): Future[Response[List[UserSearch]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val request = url(s"https://api.instagram.com/v1/users/search?$stringAuth&q=$name&count=${count.mkString}")
    Request.send[List[UserSearch]](request)
  }

  /**
   * Get the list of users this user follows.
   *
   * @param auth   Credentials.
   * @param userId Instagram ID of the user.
   * @param count  Max number of results to return.
   * @param cursor Return users after this cursor.
   * @return       A Future of a Response of a List[User].
   */
  def follows(auth: Authentication, userId: String, count: Option[Int] = None,
              cursor: Option[String] = None): Future[Response[List[User]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val request = url(
      s"https://api.instagram.com/v1/users/$userId/follows?$stringAuth&count=${count.mkString}&cursor=${cursor.mkString}"
    )
    Request.send[List[User]](request)
  }

  /**
   * Get the list of users this user is followed by.
   *
   * @param auth   Credentials.
   * @param userId Instagram ID of the user.
   * @param count  Max number of results to return.
   * @param cursor Return users after this cursor.
   * @return       A Future of a Response of a List[User].
   */
  def followedBy(auth: Authentication, userId: String, count: Option[Int] = None,
                 cursor: Option[String] = None): Future[Response[List[User]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val request = url(
      s"https://api.instagram.com/v1/users/$userId/followed-by?$stringAuth&count=${count.mkString}&cursor=${cursor.mkString}"
    )
    Request.send[List[User]](request)
  }

  /**
   * View users who has sent a follow request.
   *
   * @param auth   Credentials.
   * @param count  Max number of results to return.
   * @param cursor Return users after this cursor.
   * @return       A Future of a Response of a List[User].
   */
  def relationshipRequests(auth: Authentication, count: Option[Int] = None,
                           cursor: Option[String] = None): Future[Response[List[User]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val request = url(
      s"https://api.instagram.com/v1/users/self/requested-by?$stringAuth&count=${count.mkString}&cursor=${cursor.mkString}"
    )
    Request.send[List[User]](request)
  }

  /**
   * Get information about a relationship to another user.
   *
   * @param auth   Credentials.
   * @param userId Instagram ID of the user.
   * @return       A Future of a Response of a Relationship.
   */
  def relationship(auth: Authentication, userId: String): Future[Response[Relationship]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val request = url(s"https://api.instagram.com/v1/users/$userId/relationship?$stringAuth")
    Request.send[Relationship](request)
  }

  /**
   * Send the request to update the relationship status.
   * This method is called from the methods named relationshipXXX.
   *
   * @param auth   Credentials.
   * @param userId Instagram ID of the user.
   * @param action Action (follow/unfollow/block/unblock/approve/deny).
   * @return       A Future of a Response of a Relationship.
   */
  private def updateRelationship(auth: Authentication, userId: String, action: String): Future[Response[Relationship]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val request = url(s"https://api.instagram.com/v1/users/$userId/relationship?$stringAuth") << Map("action" -> action)
    Request.send[Relationship](request)
  }

  /**
   * Follow a user.
   *
   * @param auth   Credentials.
   * @param userId Instagram ID of the user.
   * @return       A Future of a Response of a Relationship.
   */
  def relationshipFollow(auth: Authentication, userId: String): Future[Response[Relationship]] = {
    updateRelationship(auth, userId, action = "follow")
  }

  /**
   * Unfollow a user.
   *
   * @param auth   Credentials.
   * @param userId Instagram ID of the user.
   * @return       A Future of a Response of a Relationship.
   */
  def relationshipUnfollow(auth: Authentication, userId: String): Future[Response[Relationship]] = {
    updateRelationship(auth, userId, action = "unfollow")
  }

  /**
   * Block a user.
   *
   * @param auth   Credentials.
   * @param userId Instagram ID of the user.
   * @return       A Future of a Response of a Relationship.
   */
  def relationshipBlock(auth: Authentication, userId: String): Future[Response[Relationship]] = {
    updateRelationship(auth, userId, action = "block")
  }

  /**
   * Unblock a user.
   *
   * @param auth   Credentials.
   * @param userId Instagram ID of the user.
   * @return       A Future of a Response of a Relationship.
   */
  def relationshipUnblock(auth: Authentication, userId: String): Future[Response[Relationship]] = {
    updateRelationship(auth, userId, action = "unblock")
  }

  /**
   * Approve a follow request from a user.
   *
   * @param auth   Credentials.
   * @param userId Instagram ID of the user.
   * @return       A Future of a Response of a Relationship.
   */
  def relationshipApprove(auth: Authentication, userId: String): Future[Response[Relationship]] = {
    updateRelationship(auth, userId, action = "approve")
  }

  /**
   * Ignore a follow request from a user.
   *
   * @param auth   Credentials.
   * @param userId Instagram ID of the user.
   * @return       A Future of a Response of a Relationship.
   */
  def relationshipIgnore(auth: Authentication, userId: String): Future[Response[Relationship]] = {
    updateRelationship(auth, userId, action = "ignore")
  }

  /**
   * Get information about a media object.
   *
   * @param auth    Credentials.
   * @param mediaId ID of an Instagram media.
   * @return        A Future of a Response of a Media.
   */
  def media(auth: Authentication, mediaId: String): Future[Response[Media]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val request = url(s"https://api.instagram.com/v1/media/$mediaId?$stringAuth")
    Request.send[Media](request)
  }

  /**
   * Get information about a media object.
   *
   * @param auth      Credentials.
   * @param shortcode Shortcode of an Instagram ID.
   *                  A media object's shortcode can be found in its shortlink URL.
                      An example shortlink is http://instagram.com/p/D/
                      Its corresponding shortcode is D.
   * @return          A Future of a Response of a Media.
   */
  def mediaShortcode(auth: Authentication, shortcode: String): Future[Response[Media]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val request = url(s"https://api.instagram.com/v1/media/shortcode/$shortcode?$stringAuth")
    Request.send[Media](request)
  }

  /**
   * Search for media in a given area.
   * The default time span is set to 5 days. The time span must not exceed 7 days. Defaults time stamps cover the last 5 days.
   *
   * @param auth         Credentials.
   * @param coordinates  Tuple2: Latitude & Longitude coordinates.
   * @param minTimestamp Return media after this UNIX timestamp.
   * @param maxTimestamp Return media before this UNIX timestamp.
   * @param distance     Default is 1000m (distance=1000), max distance is 5000.
   * @return             A Future of a Response of a List of Media.
   */
  def mediaSearch(auth: Authentication, coordinates: (Double, Double), minTimestamp: Option[String] = None,
                  maxTimestamp: Option[String] = None, distance: Option[Int] = None): Future[Response[List[Media]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val request = url(
      s"https://api.instagram.com/v1/media/search?$stringAuth&lat=${coordinates._1.toString}&lng=${coordinates._2.toString}" +
      s"&min_timestamp=${minTimestamp.mkString}&max_timestamp=${maxTimestamp.mkString}&distance=${distance.mkString}"
    )
    Request.send[List[Media]](request)
  }

  /**
   * Get a list of currently popular media.
   *
   * @param auth Credentials.
   * @return     A Future of a Response of a List of Media.
   */
  def popular(auth: Authentication): Future[Response[List[Media]]] = {
    val stringAuth = Authentication.toGETParams(auth)
    val request = url(s"https://api.instagram.com/v1/media/popular?$stringAuth")
    Request.send[List[Media]](request)
  }

}
