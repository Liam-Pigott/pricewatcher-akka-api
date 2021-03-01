package route

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import model.Price
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import service.PriceService
import spray.json._



trait PriceJsonProtocol extends DefaultJsonProtocol {
  implicit val priceFormat = jsonFormat6(Price)

  // spray needs custom parser for date time
  implicit object DateJsonFormat extends RootJsonFormat[DateTime] {
    private val parser : DateTimeFormatter = DateTimeFormat.forPattern("yyyy-mm-dd hh:mm:ss")
    override def write(dt: DateTime) = JsString(parser.print(dt))
    override def read(json: JsValue) : DateTime = json match {
      case JsString(s) => parser.parseDateTime(s)
      case _ => throw new Exception("Malformed datetime")
    }
  }
}

class PriceRoutes(val priceService: PriceService) extends PriceJsonProtocol with SprayJsonSupport{

  val route = path("api" / "prices") {
    get {
      complete(priceService.getPrices)
    }
  }
}
