package database

import model.table.{PriceTable, WatcherTable}

import scala.concurrent.Future

/**
 * Helper class for common db functions
 */
object DatabaseManager {

  //Handle schema creation when tables do not exist
  def initTables(dc: DatabaseComponent): Future[Unit] = {
    import dc.profile.api._
    val watchers = TableQuery[WatcherTable]
    val prices = TableQuery[PriceTable]
    dc.db.run(DBIO.seq(
      watchers.schema.createIfNotExists,
      prices.schema.createIfNotExists
    ))
  }

}
