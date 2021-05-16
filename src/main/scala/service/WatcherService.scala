package service

import database.DatabaseComponent
import model.Watcher
import model.table.WatcherTable

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class WatcherService extends DatabaseComponent{
  import dbComponent.profile.api._

  val watchers = TableQuery[WatcherTable]

  def getWatchers: Future[Seq[Watcher]] = db.run(watchers.result)

  def getWatcherById(id: Long): Future[Option[Watcher]] = db.run(watchers.filter(_.id === id).result.headOption)

  def createWatcher(watcher: Watcher): Future[Watcher] = getWatcherByUrlXpath(watcher.url, watcher.xpath).flatMap {
    case true => throw new IllegalArgumentException(s"Watcher with url=${watcher.url} and xpath=${watcher.xpath} already exists!")
    case _ =>
      db.run(watchers returning watchers.map(_.id) into ((watcher, id) => watcher.copy(id = Some(id))) += watcher)
  }

  def updateWatcher(id: Long, toUpdate: Watcher): Future[Option[Watcher]] = getWatcherById(id).flatMap {
    case Some(_) =>
      val updatedWatcher = toUpdate.copy(id=Some(id))
      db.run(watchers.filter(_.id === id).update(updatedWatcher)).map(_ => Some(updatedWatcher))
    case None => Future.successful(None)
  }

  def deleteWatcher(id: Long): Future[Int] = db.run(watchers.filter(_.id === id).delete)

  def getWatcherByUrlXpath(url: String, xpath: String): Future[Boolean] = db.run(watchers.filter(w => w.url === url && w.xpath === xpath).exists.result)
}