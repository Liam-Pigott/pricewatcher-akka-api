package route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import mapper.PriceJsonProtocol
import service.PriceService
import util.Logging

//TODO: add date range GET
class PriceRoutes(val priceService: PriceService) extends PriceJsonProtocol with Logging {

  val routes: Route = {
    pathPrefix("api" / "prices") {
      get {
        pathEndOrSingleSlash {
          complete(priceService.getPrices)
        } ~
          path(IntNumber) { id =>
            onSuccess(priceService.getPriceById(id)) {
              case Some(price) => complete(price)
              case None => {
                logger.info(s"Attempt to find price entry with id $id failed")
                complete(StatusCodes.NotFound)
              }
            }
          }
      }
    }
  }
}
