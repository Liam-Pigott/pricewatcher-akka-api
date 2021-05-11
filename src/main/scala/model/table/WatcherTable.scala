package model.table

import model.Watcher
import slick.lifted.{ProvenShape, Tag}
import slick.jdbc.MySQLProfile.api._

class WatcherTable(tag: Tag) extends Table[Watcher](tag, "Watcher"){
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def url = column[String]("url")
  def xpath = column[String]("xpath")

  def * : ProvenShape[Watcher] = (id.?, name, url, xpath) <> (Watcher.tupled, Watcher.unapply)
}