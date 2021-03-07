package price

import model.Price
import org.joda.time.DateTime

trait PriceData {
  val testPrice1: Price = Price(Some(1L), "Test1", DateTime.now, "https://testurl.com/1", 100, None)
  val testPrice2: Price = Price(Some(2L), "Test2", DateTime.now, "https://testurl.com/2", 30, Some("Coffee"))
  val testPrice3: Price = Price(None, "Test3", DateTime.now, "https://testurl.com/3", 30, Some("Category"))
}
