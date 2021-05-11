package watcher

import model.Watcher

trait WatcherData {
  val testWatcher1: Watcher = Watcher(Some(1L), "name", "url", "xpath")
  val testWatcher2: Watcher = Watcher(Some(2L), "guitar", "https://www.amazon.co.uk/guitar1", "//*[@id=\"guitar1\"]/div[1]/span")
  val testWatcher3: Watcher = Watcher(None, "coffee", "https://perkyblenders.com/products/sow-blend-3?variant=34399500009531", "//*[@id=\"product-form-page-product-coffee\"]/div[6]/span[1]/span[1]")

  val testWatchers = Seq(testWatcher1, testWatcher2, testWatcher3)
}
