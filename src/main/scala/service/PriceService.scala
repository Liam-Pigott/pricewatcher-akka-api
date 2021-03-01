package service

import model.Price
import org.joda.time.DateTime

class PriceService {

  def getPrices: Price = Price(1, "test", DateTime.now, "test_url", 100.0, None)
}
