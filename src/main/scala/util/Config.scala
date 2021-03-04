package util

import com.typesafe.config.ConfigFactory

trait Config {
  private val config = ConfigFactory.load()
  private val dbConfig = config.getConfig("pricewatcher-mysql")
  private val httpConfig = config.getConfig("http")

  // Server Config
  val host: String = httpConfig.getString("host")
  val port: Int = httpConfig.getInt("port")

  //Database config
  val dbDriver: String = dbConfig.getString("driver")
  val dbUrl: String = dbConfig.getString("url")
  val dbUser: String = dbConfig.getString("user")
  val dbPass: String = dbConfig.getString("password")

}
