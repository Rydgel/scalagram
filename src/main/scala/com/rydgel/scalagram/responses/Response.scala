package com.rydgel.scalagram.responses

trait InstagramData

sealed trait Response[+T] {
  def isOk: Boolean
  def isError: Boolean
}
case class ResponseError[T](meta: Meta) extends Response[T] {
  def isOk: Boolean = false
  def isError: Boolean = true
}
case class ResponseOK[T](data: InstagramData, pagination: Option[Pagination], meta: Meta) extends Response[T] {
  def isOk: Boolean = true
  def isError: Boolean = false
}

sealed trait Parse[+T]
case class ParseError[T](error: Meta) extends Parse[T]
case class ParseOk[T](data: InstagramData) extends Parse[T]
