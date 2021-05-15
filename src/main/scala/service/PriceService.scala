package service

import database.DatabaseComponent._
import model.Price
import model.table.PriceTable
import org.joda.time.DateTime

import java.sql.Timestamp
import scala.concurrent.Future

/**
 * The purpose of this service is to expose data already collected rather than generate/update existing entries
 */
class PriceService {
  import dbComponent.profile.api._

  val prices = TableQuery[PriceTable]

  def getPrices: Future[Seq[Price]] = db.run(prices.result)

  def getPriceById(id: Long): Future[Option[Price]] = db.run(prices.filter(_.id === id).result.headOption)

  def getPricesForDateRange(startDate: DateTime, endDate: DateTime): Future[Seq[Price]] = {
    //https://stackoverflow.com/questions/26433721/joda-date-mapper-for-slick-mappedcolumntyoe
    implicit def jodaTimeMapping: BaseColumnType[DateTime] = MappedColumnType.base[DateTime, Timestamp](
      dateTime => new Timestamp(dateTime.getMillis),
      timeStamp => new DateTime(timeStamp.getTime)
    )

    db.run(prices.filter {
      price => price.date > startDate && price.date < endDate
    }.result)
  }

  def getPricesForWatcher(watcherId: Long): Future[Seq[Price]] = {
    db.run(prices.filter(_.watcherId === watcherId).result)
  }
}
