package watcher

import akka.http.scaladsl.testkit.ScalatestRouteTest
import mapper.WatcherJsonProtocol
import model.Watcher
import org.mockito.Mockito.when
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import org.scalatestplus.mockito.MockitoSugar.mock
import route.WatcherRoutes
import service.WatcherService
import spray.json.enrichAny

import scala.concurrent.Future

class WatcherRoutesTest  extends AnyWordSpec with Matchers with ScalatestRouteTest with WatcherData with WatcherJsonProtocol{

  val mockWatcherService = mock[WatcherService]
  val routes = new WatcherRoutes(mockWatcherService).routes

  "WatcherRoutes" should {
    "return empty list of watchers if none exist (GET / watchers)" in {
      when(mockWatcherService.getWatchers).thenReturn(Future.successful(Seq()))

      Get("/api/watchers") ~> routes ~> check {
        responseAs[String] shouldBe "[]"
        status.intValue() shouldBe 200
      }
    }
    "return list of all watchers if they exist (GET / watchers)" in {
      when(mockWatcherService.getWatchers).thenReturn(Future.successful(Seq(testWatcher1, testWatcher2)))

      Get("/api/watchers") ~> routes ~> check {
        responseAs[String] shouldBe Seq(testWatcher1, testWatcher2).toJson.toString
        status.intValue() shouldBe 200
      }
    }


    "return watcher when using valid id (GET / watchers / :id)" in {
      when(mockWatcherService.getWatcherById(1L)).thenReturn(Future.successful(Some(testWatcher1)))

      Get("/api/watchers/1") ~> routes ~> check {
        responseAs[Watcher].id shouldBe testWatcher1.id
        responseAs[Watcher].name shouldBe testWatcher1.name
        responseAs[Watcher].url shouldBe testWatcher1.url
        responseAs[Watcher].xpath shouldBe testWatcher1.xpath
        status.intValue() shouldBe 200
      }
    }

    "return 404 when using invalid id (GET / watchers / :id)" in {
      when(mockWatcherService.getWatcherById(999L)).thenReturn(Future.successful(None))

      Get("/api/watchers/999") ~> routes ~> check {
        status.intValue() shouldBe 404
      }
    }

    "return watcher when create watcher (POST / watchers)" in {
      when(mockWatcherService.createWatcher(testWatcher1)).thenReturn(Future.successful(testWatcher1))

      Post("/api/watchers", testWatcher1) ~> routes ~> check {
        status.intValue() shouldBe 200
        responseAs[Watcher].name shouldBe testWatcher1.name
        responseAs[Watcher].url shouldBe testWatcher1.url
        responseAs[Watcher].xpath shouldBe testWatcher1.xpath
      }
    }

    "throw when create duplicate watcher (POST / watchers)" in {
      when(mockWatcherService.createWatcher(testWatcher1)).thenThrow(new IllegalArgumentException("watcher already exists!"))

      Post("/api/watchers", testWatcher1) ~> routes ~> check {
        status.intValue() shouldBe 409
        responseAs[String] should include ("already exists!")
      }
    }

    "return watcher when update watcher (PUT / watchers / :id)" in {
      when(mockWatcherService.updateWatcher(1L, testWatcher2)).thenReturn(Future.successful(Some(testWatcher2.copy(id = Some(1L)))))

      Put("/api/watchers/1", testWatcher2) ~> routes ~> check {
        status.intValue() shouldBe 200
        responseAs[Watcher].id shouldBe Some(1L)
        responseAs[Watcher].name shouldBe testWatcher2.name
        responseAs[Watcher].url shouldBe testWatcher2.url
        responseAs[Watcher].xpath shouldBe testWatcher2.xpath
      }
    }

    "return not found when update watcher (PUT / watchers / :id)" in {
      when(mockWatcherService.updateWatcher(999L, testWatcher1)).thenReturn(Future.successful(None))

      Put("/api/watchers/999", testWatcher1) ~> routes ~> check {
        status.intValue() shouldBe 204
      }
    }

    "return status 200 when delete watcher (DELETE / watchers / :id)" in {
      when(mockWatcherService.deleteWatcher(1L)).thenReturn(Future.successful(1))

      Delete("/api/watchers/1") ~> routes ~> check {
        status.intValue() shouldBe 200
      }
    }

    "return status 204 when delete watcher not exists (DELETE / watchers / :id)" in {
      when(mockWatcherService.deleteWatcher(999L)).thenReturn(Future.successful(0))

      Delete("/api/watchers/999") ~> routes ~> check {
        status.intValue() shouldBe 204
      }
    }
  }
}