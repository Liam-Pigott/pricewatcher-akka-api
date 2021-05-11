package model

case class Watcher(id: Option[Long] = None, name: String, url: String, xpath: String)
