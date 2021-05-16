package route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import mapper.WatcherJsonProtocol
import model.Watcher
import service.WatcherService
import util.Logging


class WatcherRoutes(val watcherService: WatcherService) extends WatcherJsonProtocol with Logging with CustomRouteExceptions {

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
      } ~
        post {
          handleExceptions(illegalArgumentExceptionHandler) {
            entity(as[Watcher]) { watcher =>
              complete(watcherService.createWatcher(watcher))
            }
          }
        } ~
        put {
          path(IntNumber) { id =>
            entity(as[Watcher]) { watcher =>
              onSuccess(watcherService.updateWatcher(id, watcher)) {
                case Some(watcher) => complete(watcher)
                case _ => complete(StatusCodes.NoContent)
              }
            }
          }
        } ~
        (delete & path(IntNumber)) { id =>
          onSuccess(watcherService.deleteWatcher(id)) {
            case 1 => complete(StatusCodes.OK)
            case _ => complete(StatusCodes.NoContent)
          }
        }
    }
  }
}
