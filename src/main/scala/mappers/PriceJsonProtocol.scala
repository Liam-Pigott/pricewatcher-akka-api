package mappers

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import model.Price
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

trait PriceJsonProtocol extends DefaultJsonProtocol with SprayJsonSupport{

  import CustomDateTimeFormat._

  implicit val priceFormat: RootJsonFormat[Price] = jsonFormat6(Price)
}