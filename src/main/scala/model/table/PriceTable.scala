package model.table

import model.Price
import org.joda.time.DateTime
import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.MySQLProfile.api._
import com.github.tototoshi.slick.MySQLJodaSupport._


class PriceTable(tag: Tag) extends Table[Price](tag, "prices"){
  def id = column[Option[Long]]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def date = column[DateTime]("date", O.SqlType("DATETIME"))
  def url = column[String]("url")
  def price = column[Double]("price")
  def category = column[Option[String]]("category")

  def * : ProvenShape[Price] = (id, name, date, url, price, category) <> (Price.tupled, Price.unapply)
}




