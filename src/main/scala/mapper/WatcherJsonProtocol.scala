package mapper

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import model.Watcher
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait WatcherJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport{

  implicit val watcherFormat: RootJsonFormat[Watcher] = jsonFormat4(Watcher)
}