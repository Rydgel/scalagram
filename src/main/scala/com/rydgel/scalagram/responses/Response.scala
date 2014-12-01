package com.rydgel.scalagram.responses

import com.ning.http.client.FluentCaseInsensitiveStringsMap

case class Response[T](data: Option[T], pagination: Option[Pagination], meta: Meta, headers: FluentCaseInsensitiveStringsMap)
