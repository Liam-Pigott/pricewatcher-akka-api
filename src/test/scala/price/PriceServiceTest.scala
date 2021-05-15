package price

import database.{DatabaseComponent, DatabaseManager}
import mapper.CustomDateTimeFormat
import model.table.PriceTable
import org.joda.time.DateTime
import util.Config
import org.scalatest._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import service.PriceService
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class PriceServiceTest extends AnyWordSpec with BeforeAndAfter with BeforeAndAfterEach with Config with PriceData with DatabaseComponent {

  val priceService: PriceService = new PriceService
  val priceTable = TableQuery[PriceTable]

  before {
    Await.result(DatabaseManager.initTables(dbComponent), Duration.Inf)
  }

  override def afterEach() = {
    Await.result(db.run(priceTable.delete), Duration.Inf)
  }

  "PriceService" should {

    "return empty prices when table is empty" in {
      val prices = Await.result(priceService.getPrices, Duration.Inf)
      assert(prices.isEmpty)
    }

    "return price by id when id exists" in {
      val priceToTest = testPrice1
      // Add the entry to fetch later from service layer
      Await.result(db.run(priceTable += priceToTest), Duration.Inf)

      // get by test id
      val res = Await.result(priceService.getPriceById(priceToTest.id.head), Duration.Inf)
      assert(priceToTest.id === res.head.id)
    }

    "return list of prices" in {
      // Add the entry to fetch later from service layer
      Await.result(db.run(priceTable ++= testPrices), Duration.Inf)

      // get by test id
      val res = Await.result(priceService.getPrices, Duration.Inf)
      assert(res.size.equals(3))
      assert(res.head.watcher_id.equals(testPrice1.watcher_id))
      assert(res.head.price.equals(testPrice1.price))
    }

    "return empty price when get by id if id not exists" in {
      val res = Await.result(priceService.getPriceById(999L), Duration.Inf)
      assert(res.isEmpty)
    }

    "return prices for date range" in {
      // Add the entry to fetch later from service layer
      Await.result(db.run(priceTable ++= testPrices), Duration.Inf)

      // get by test id
      val res = Await.result(priceService.getPricesForDateRange(CustomDateTimeFormat.parseDateTimeString("2021-01-01 11:11:11"), DateTime.now()), Duration.Inf)
      assert(res.size == 2)
    }
  }

  "return prices for watcher id" in {
    Await.result(db.run(priceTable ++= testPrices), Duration.Inf)

    val res = Await.result(priceService.getPricesForWatcher(1L), Duration.Inf)
    assert(res.size == 2)
  }

}
