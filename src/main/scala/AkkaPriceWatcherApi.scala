import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import com.typesafe.config.ConfigFactory
import route.PriceRoutes
import service.PriceService

object AkkaPriceWatcherApi{

  implicit val system = ActorSystem(Behaviors.empty, "AkkaPriceWatcherApi")

  val config = ConfigFactory.load()
  val dbConfig = config.getConfig("pricewatcherdb")


  val priceService = new PriceService
  val priceRoutes = new PriceRoutes(priceService)
  val route: Route = priceRoutes.route

  def main(args: Array[String]): Unit = {
    Http().newServerAt("localhost", 8082).bind(route)
  }
}
