package price

import akka.http.scaladsl.testkit.ScalatestRouteTest
import mappers.PriceJsonProtocol
import model.Price
import org.mockito.Mockito.when
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock
import route.PriceRoutes
import service.PriceService

import scala.concurrent.Future

class PriceRoutesTest extends AnyWordSpec with Matchers with ScalatestRouteTest with PriceData with PriceJsonProtocol {

  val mockPriceService = mock[PriceService]
  val routes = new PriceRoutes(mockPriceService).routes

  "PriceRoutes" should {
    "return no prices if none exist (GET / prices)" in {
      when(mockPriceService.getPrices).thenReturn(Future.successful(Seq()))

      Get("/api/prices") ~> routes ~> check {
        responseAs[String] shouldBe "[]"
        status.intValue() shouldBe 200
      }
    }

    "return price when using valid id" in {
      when(mockPriceService.getPriceById(1L)).thenReturn(Future.successful(Some(testPrice1)))

      Get("/api/prices/1") ~> routes ~> check {
        responseAs[Price].id shouldBe testPrice1.id
        responseAs[Price].name shouldBe testPrice1.name
        status.intValue() shouldBe 200
      }
    }

    "return 404 when using invalid id" in {
      when(mockPriceService.getPriceById(999L)).thenReturn(Future.successful(None))

      Get("/api/prices/999") ~> routes ~> check {
        status.intValue() shouldBe 404
      }
    }

//    "return list of all prices if they exist (GET / prices)" in {
//      when(mockPriceService.getPrices).thenReturn(Future.successful(Seq(testPrice1, testPrice2)))
//
//      Get("/api/prices") ~> routes ~> check {
//        responseAs[String] shouldBe Seq(testPrice1, testPrice2).asJson
//        status.intValue() shouldBe 200
//      }
//    }
  }
}

