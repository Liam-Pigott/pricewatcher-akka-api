package mapper

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import model.Price
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait PriceJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport with CustomDateTimeFormat {

  implicit val priceFormat: RootJsonFormat[Price] = jsonFormat4(Price)
}