package model

import org.joda.time.DateTime

case class Price(id: Long, name: String, date: DateTime, url: String, price: Double, category: Option[String])