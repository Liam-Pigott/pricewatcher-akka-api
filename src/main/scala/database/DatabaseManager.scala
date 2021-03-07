package database

import model.table.PriceTable
import scala.concurrent.Future

/**
 * Helper class for common db functions
 */
object DatabaseManager {

  //Handle schema creation when tables do not exist
  def initTables(dc: DatabaseComponent): Future[Unit] = {
    import dc.profile.api._
    val prices = TableQuery[PriceTable]
    dc.db.run(prices.schema.createIfNotExists)
  }

}
