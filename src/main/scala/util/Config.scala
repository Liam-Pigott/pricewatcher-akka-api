package util

import com.typesafe.config.ConfigFactory

trait Config {
  private val config = ConfigFactory.load()
  private val httpConfig = config.getConfig("http")

  // Server Config
  val host: String = httpConfig.getString("host")
  val port: Int = httpConfig.getInt("port")

  //Database config
  val defaultDb: String = config.getString("default-database")
}
