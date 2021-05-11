package model.table

import model.Price
import org.joda.time.DateTime
import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.MySQLProfile.api._
import com.github.tototoshi.slick.MySQLJodaSupport._


class PriceTable(tag: Tag) extends Table[Price](tag, "Price"){
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def watcherId = column[Long]("watcher_id")
  def price = column[Double]("price")
  def date = column[DateTime]("date_time", O.SqlType("DATETIME"))

  def watcher = foreignKey("FK__Price__Watcher", watcherId, TableQuery[WatcherTable])(_.id)

  def * : ProvenShape[Price] = (id.?, watcherId, price, date) <> (Price.tupled, Price.unapply)
}