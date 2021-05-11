package route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import mapper.{PriceJsonProtocol, WatcherJsonProtocol}
import org.slf4j.LoggerFactory
import service.{PriceService, WatcherService}


class WatcherRoutes(val watcherService: WatcherService) extends WatcherJsonProtocol{

  val log = LoggerFactory.getLogger(this.getClass)

  val routes: Route = {
    pathPrefix("api" / "watchers") {
      get {
        pathEndOrSingleSlash {
          complete(watcherService.getWatchers)
        } ~
          path(IntNumber) { id =>
            onSuccess(watcherService.getWatcherById(id)) {
              case Some(watcher) => complete(watcher)
              case None => {
                log.info(s"Attempt to find watcher entry with id $id failed")
                complete(StatusCodes.NotFound)
              }
            }
          }
      }
    }
  }
}
