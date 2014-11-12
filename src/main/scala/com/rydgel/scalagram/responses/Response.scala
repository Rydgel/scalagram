package com.rydgel.scalagram.responses

trait InstagramData

sealed trait Response[+T]
case class ResponseError[T](value: Meta) extends Response[T]
case class ResponseOK[T](data: InstagramData, pagination: Option[Pagination], meta: Meta) extends Response[T]

sealed trait Parse[+T]
case class ParseError[T](error: Meta) extends Parse[T]
case class ParseOk[T](data: InstagramData) extends Parse[T]
