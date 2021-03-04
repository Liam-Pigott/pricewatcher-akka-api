package price

import model.Price
import org.joda.time.DateTime

trait PriceData {
  val testPrice1: Price = Price(1L, "Test1", DateTime.now, "https://testurl.com/1", 100, None)
  val testPrice2: Price = Price(2L, "Test2", DateTime.now, "https://testurl.com/2", 30, Some("Coffee"))
}
