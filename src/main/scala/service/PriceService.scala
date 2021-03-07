package service

import database.DatabaseComponent
import model.Price
import model.table.PriceTable

import scala.concurrent.Future

/**
 * The purpose of this service is to expose data already collected rather than generate/update existing entries
 */
class PriceService(val databaseComponent: DatabaseComponent){
  import databaseComponent._
  import databaseComponent.profile.api._

  val prices = TableQuery[PriceTable]

  def getPrices: Future[Seq[Price]] = db.run(prices.result)

  def getPriceById(id: Long): Future[Option[Price]] = db.run(prices.filter(_.id === id).result.headOption)
}
