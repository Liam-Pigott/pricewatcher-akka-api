package model

import org.joda.time.DateTime

case class Price(id: Option[Long] = None, watcher_id: Long, price: Double, date: DateTime)