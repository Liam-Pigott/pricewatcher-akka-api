package service

import database.DatabaseComponent
import model.Price
import model.table.PriceTable
import slick.lifted.TableQuery
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.Future

/**
 * The purpose of this service is to expose data already collected rather than generate/update existing entries
 */
class PriceService extends DatabaseComponent{

  val pricesTable = TableQuery[PriceTable]

  def getPrices: Future[Seq[Price]] = db.run(pricesTable.result)

  def getPriceById(id: Long): Future[Option[Price]] = db.run(pricesTable.filter(_.id === id).result.headOption)
}
