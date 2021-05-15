package database

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile
import util.Config

trait DatabaseComponent extends Config {

  val dc = DatabaseConfig.forConfig[JdbcProfile](defaultDb)
  val db = dc.db
  val profile = dc.profile

  val dbComponent: DatabaseComponent = this
}

object DatabaseComponent extends DatabaseComponent
