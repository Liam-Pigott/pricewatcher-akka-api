package route

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives.complete
import akka.http.scaladsl.server.ExceptionHandler

/**
 * Use this class to define custom exceptions and status codes for API responses
 */
trait CustomRouteExceptions {

  implicit def illegalArgumentExceptionHandler: ExceptionHandler =
    ExceptionHandler {
      case e: IllegalArgumentException =>
        complete(HttpResponse(StatusCodes.Conflict, entity = s"${e.getMessage}"))
    }
}
