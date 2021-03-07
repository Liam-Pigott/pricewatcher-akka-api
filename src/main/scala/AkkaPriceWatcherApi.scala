import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import database.{DatabaseComponent, DatabaseManager}
import route.PriceRoutes
import service.PriceService
import util.Config

object AkkaPriceWatcherApi extends Config {

  implicit val system = ActorSystem(Behaviors.empty, "AkkaPriceWatcherApi")

  val dbComponent = new DatabaseComponent(defaultDb)

  DatabaseManager.initTables(dbComponent)

  val priceService = new PriceService(dbComponent)
  val priceRoutes = new PriceRoutes(priceService)
  val route: Route = priceRoutes.routes

  def main(args: Array[String]): Unit = {
    Http().newServerAt(host, port).bind(route)
  }
}
