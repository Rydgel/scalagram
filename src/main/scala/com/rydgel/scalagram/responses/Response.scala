package com.rydgel.scalagram.responses

trait InstagramData

sealed trait Response[+T] {
  def isOk: Boolean
  def isError: Boolean
  val data: Option[T]
  val pagination: Option[Pagination]
  val meta: Meta
}
case class ResponseError[T](meta: Meta) extends Response[T] {
  def isOk: Boolean = false
  def isError: Boolean = true
  val data = None
  val pagination = None
}
case class ResponseOK[T](data: Option[T], pagination: Option[Pagination], meta: Meta) extends Response[T] {
  def isOk: Boolean = true
  def isError: Boolean = false
}

sealed trait Parse[+T]
case class ParseError[T](error: Meta) extends Parse[T]
case class ParseOk[T](data: T) extends Parse[T]
