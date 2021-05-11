import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import database.{DatabaseComponent, DatabaseManager}
import route.{PriceRoutes, WatcherRoutes}
import service.{PriceService, WatcherService}
import util.Config
import akka.http.scaladsl.server.Directives._

object AkkaPriceWatcherApi extends Config {

  implicit val system = ActorSystem(Behaviors.empty, "AkkaPriceWatcherApi")

  val dbComponent = new DatabaseComponent(defaultDb)

  DatabaseManager.initTables(dbComponent)

  val watcherService = new WatcherService(dbComponent)
  val watcherRoutes = new WatcherRoutes(watcherService)

  val priceService = new PriceService(dbComponent)
  val priceRoutes = new PriceRoutes(priceService)

  val routes: Route = priceRoutes.routes ~ watcherRoutes.routes

  def main(args: Array[String]): Unit = {
    Http().newServerAt(host, port).bind(routes)
  }
}
