package watcher

import database.{DatabaseComponent, DatabaseManager}
import model.Watcher
import model.table.WatcherTable
import org.scalatest.{BeforeAndAfter, BeforeAndAfterEach}
import org.scalatest.wordspec.AnyWordSpec
import service.WatcherService
import slick.jdbc.JdbcBackend
import slick.lifted.TableQuery
import util.Config
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class WatcherServiceTest extends AnyWordSpec with BeforeAndAfter with BeforeAndAfterEach with Config with WatcherData {

  var dc: DatabaseComponent = _
  var db: JdbcBackend#DatabaseDef = _
  var watcherService: WatcherService = _
  val watcherTable = TableQuery[WatcherTable]


  before {
    dc = new DatabaseComponent(defaultDb) // default db specified in application.conf
    Await.result(DatabaseManager.initTables(dc), Duration.Inf)
    db = dc.db
    watcherService = new WatcherService(dc)
  }

  override def afterEach() = {
    Await.result(db.run(watcherTable.schema.truncate), Duration.Inf)
  }

  "WatcherService" should {

    "return empty watchers when table is empty" in {
      val res = Await.result(watcherService.getWatchers, Duration.Inf)
      assert(res.isEmpty)
    }

    "return watcher when created" in {
      val watcherWithoutId = testWatcher3
      val res = Await.result(watcherService.createWatcher(testWatcher3), Duration.Inf)

      assert(res.id.head === 1)
      assert(res.name === watcherWithoutId.name)
      assert(res.url === watcherWithoutId.url)
      assert(res.xpath=== watcherWithoutId.xpath)
    }

    "return watcher by id when id exists" in {
      val origWatcher = testWatcher1
      val id = addWatcherReturnId(origWatcher)
      val res = Await.result(watcherService.getWatcherById(id), Duration.Inf).head
      assert(res.id === Some(id))
      assert(res.xpath === origWatcher.xpath)
    }

    "return list of watchers" in {
      Await.result(db.run(watcherTable ++= testWatchers), Duration.Inf)

      val res = Await.result(watcherService.getWatchers, Duration.Inf)
      assert(res.size.equals(3))
      assert(res.head.xpath.equals(testWatcher1.xpath))
      assert(res.head.url.equals(testWatcher1.url))
    }

    "return empty watcher when get by id if id not exists" in {
      val res = Await.result(watcherService.getWatcherById(999L), Duration.Inf)
      assert(res.isEmpty)
    }

    "return updated watcher when calling update" in {
      val origWatcher = testWatcher1
      val toUpdateId = addWatcherReturnId(origWatcher)
      val newName = "Updated name"
      val res = Await.result(watcherService.updateWatcher(toUpdateId,
        Watcher(None, newName, origWatcher.url, origWatcher.xpath)), Duration.Inf
      ).head

      assert(res.id === Some(toUpdateId))
      assert(res.name === newName)
      assert(res.url === origWatcher.url)
      assert(res.xpath === origWatcher.xpath)
    }
  }

  "should delete watcher" in {
    val toTest = testWatcher2
    val toDeleteId = addWatcherReturnId(toTest)
    val res = Await.result(watcherService.deleteWatcher(toDeleteId), Duration.Inf)
    assert(res === 1)

    val res2 = Await.result(watcherService.deleteWatcher(999L), Duration.Inf)
    assert(res2 === 0)
  }


  def addWatcherReturnId(watcher: Watcher): Long = {
    Await.result(db.run(watcherTable returning watcherTable.map(_.id) += watcher), Duration.Inf)
  }
}
