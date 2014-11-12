package com.rydgel.scalagram.responses

sealed trait InstagramError
trait InstagramData
case class Response[T](data: Option[T], pagination: Option[Pagination], meta: Meta)
