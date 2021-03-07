package database

import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

class DatabaseComponent(config: String){

  val dc = DatabaseConfig.forConfig[JdbcProfile](config)
  val db = dc.db
  val profile = dc.profile

}
