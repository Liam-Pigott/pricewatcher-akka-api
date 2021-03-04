package database

import slick.jdbc.JdbcBackend.{Database, Session}
import util.Config

trait DatabaseComponent extends Config {

  // Init DB
  val db = Database.forURL(
    url = dbUrl,
    user = dbUser,
    password = dbPass,
    driver = dbDriver
  )
}
