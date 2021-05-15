package price

import akka.http.scaladsl.testkit.ScalatestRouteTest
import mapper.PriceJsonProtocol
import model.Price
import org.mockito.Mockito.when
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock
import route.PriceRoutes
import service.PriceService
import spray.json._
import mapper.CustomDateTimeFormat._

import scala.concurrent.Future

class PriceRoutesTest extends AnyWordSpec with Matchers with ScalatestRouteTest with PriceData with PriceJsonProtocol {

  val mockPriceService = mock[PriceService]
  val routes = new PriceRoutes(mockPriceService).routes

  "PriceRoutes" should {
    "return empty list of prices if none exist (GET / prices)" in {
      when(mockPriceService.getPrices).thenReturn(Future.successful(Seq()))

      Get("/api/prices") ~> routes ~> check {
        responseAs[String] shouldBe "[]"
        status.intValue() shouldBe 200
      }
    }
    "return list of all prices if they exist (GET / prices)" in {
      when(mockPriceService.getPrices).thenReturn(Future.successful(Seq(testPrice1, testPrice2)))

      Get("/api/prices") ~> routes ~> check {
        responseAs[String] shouldBe Seq(testPrice1, testPrice2).toJson.toString
        status.intValue() shouldBe 200
      }
    }


    "return price when using valid id (GET / prices / :id)" in {
      when(mockPriceService.getPriceById(1L)).thenReturn(Future.successful(Some(testPrice1)))

      Get("/api/prices/1") ~> routes ~> check {
        responseAs[Price].id shouldBe testPrice1.id
        responseAs[Price].price shouldBe testPrice1.price
        responseAs[Price].watcher_id shouldBe testPrice1.watcher_id
        status.intValue() shouldBe 200
      }
    }

    "return 404 when using invalid id (GET / prices / :id)" in {
      when(mockPriceService.getPriceById(999L)).thenReturn(Future.successful(None))

      Get("/api/prices/999") ~> routes ~> check {
        status.intValue() shouldBe 404
      }
    }

    "return prices in date range" in {
      when(mockPriceService.getPricesForDateRange(parseDateTimeString("2021-01-01"), parseDateTimeString("2021-05-01")))
        .thenReturn(Future.successful(Seq(testPrice2, testPrice3)))

      Get("/api/prices/daterange?startDate=2021-01-01&endDate=2021-05-01") ~> routes ~> check {
        responseAs[String] shouldBe Seq(testPrice2, testPrice3).toJson.toString
        status.intValue() shouldBe 200
      }
    }
  }
}

