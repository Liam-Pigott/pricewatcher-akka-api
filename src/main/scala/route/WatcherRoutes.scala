package route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import mapper.WatcherJsonProtocol
import service.WatcherService
import util.Logging


class WatcherRoutes(val watcherService: WatcherService) extends WatcherJsonProtocol with Logging {

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
                logger.info(s"Attempt to find watcher entry with id $id failed")
                complete(StatusCodes.NotFound)
              }
            }
          }
      }
    }
  }
}
