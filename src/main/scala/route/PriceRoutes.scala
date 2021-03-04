package route

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import mappers.PriceJsonProtocol
import org.slf4j.LoggerFactory
import service.PriceService


class PriceRoutes(val priceService: PriceService) extends PriceJsonProtocol{

  val log = LoggerFactory.getLogger(this.getClass)

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
                log.info(s"Attempt to find price entry with id $id failed")
                complete(StatusCodes.NotFound)
              }
            }
          }
      }
    }
  }
}
