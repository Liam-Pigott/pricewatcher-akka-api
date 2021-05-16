package service

import database.DatabaseComponent
import mapper.CustomDateTimeFormat
import model.Price
import model.table.PriceTable
import org.joda.time.DateTime

import scala.concurrent.Future

/**
 * The purpose of this service is to expose data already collected rather than generate/update existing entries
 */
class PriceService extends CustomDateTimeFormat with DatabaseComponent{
  import dbComponent.profile.api._

  val prices = TableQuery[PriceTable]

  def getPrices: Future[Seq[Price]] = db.run(prices.result)

  def getPriceById(id: Long): Future[Option[Price]] = db.run(prices.filter(_.id === id).result.headOption)

  def getPricesForDateRange(startDate: DateTime, endDate: DateTime): Future[Seq[Price]] = {
    db.run(prices.filter {
      price => price.date > startDate && price.date < endDate
    }.result)
  }

  def getPricesForWatcher(watcherId: Long): Future[Seq[Price]] = {
    db.run(prices.filter(_.watcherId === watcherId).result)
  }
}
