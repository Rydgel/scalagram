package com.rydgel.scalagram.responses

case class Response[T](data: Option[T], pagination: Option[Pagination], meta: Meta)
