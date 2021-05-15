import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import database.{DatabaseComponent, DatabaseManager}
import route.{PriceRoutes, WatcherRoutes}
import service.{PriceService, WatcherService}
import util.Config
import akka.http.scaladsl.server.Directives._

object AkkaPriceWatcherApi extends Config with DatabaseComponent {

  implicit val system = ActorSystem(Behaviors.empty, "AkkaPriceWatcherApi")

  DatabaseManager.initTables(dbComponent)

  val watcherRoutes = new WatcherRoutes(new WatcherService)
  val priceRoutes = new PriceRoutes(new PriceService)

  val routes: Route = priceRoutes.routes ~ watcherRoutes.routes

  def main(args: Array[String]): Unit = {
    Http().newServerAt(host, port).bind(routes)
  }
}
