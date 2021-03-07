package price

import database.{DatabaseComponent, DatabaseManager}
import model.Price
import model.table.PriceTable
import org.joda.time.DateTime
import util.Config
import org.scalatest._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import service.PriceService
import slick.jdbc.JdbcBackend
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class PriceServiceTest extends AnyWordSpec with BeforeAndAfter with Config with PriceData {

  var dc: DatabaseComponent = _
  var db: JdbcBackend#DatabaseDef = _
  var priceService: PriceService = _
  val priceTable = TableQuery[PriceTable]


  before {
    dc = new DatabaseComponent(defaultDb) // default db specified in application.conf
    DatabaseManager.initTables(dc)
    db = dc.db
    priceService = new PriceService(dc)
  }

  after {
    db.close()
  }

  "PriceService" should {

    "return empty prices when table is empty" in {
      val prices = Await.result(priceService.getPrices, Duration.Inf)
      prices.isEmpty
    }

    "return list of prices" in {
      val prices = Seq(testPrice1, testPrice2, testPrice3)
      // Add the entry to fetch later from service layer
      Await.result(db.run(priceTable ++= prices), Duration.Inf)

      // get by test id
      val priceResult = Await.result(priceService.getPrices, Duration.Inf)
      assert(priceResult.size.equals(3))
      assert(priceResult.head.name.equals(testPrice1.name))
    }

    "return empty price when get by id if id not exists" in {
      val priceResult = Await.result(priceService.getPriceById(999L), Duration.Inf)
      assert(priceResult.isEmpty)
    }

    "return price by id when id exists" in {
      val priceToTest = testPrice1
      // Add the entry to fetch later from service layer
      Await.result(db.run(priceTable += priceToTest), Duration.Inf)

      // get by test id
      val priceResult = Await.result(priceService.getPriceById(priceToTest.id.head), Duration.Inf)
      assert(priceToTest.id === priceResult.head.id)
    }
  }


}
