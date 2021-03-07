package model

import org.joda.time.DateTime

case class Price(id: Option[Long] = None, name: String, date: DateTime, url: String, price: Double, category: Option[String])