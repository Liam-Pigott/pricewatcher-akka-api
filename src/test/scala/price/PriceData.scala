package price

import mapper.CustomDateTimeFormat
import model.Price

trait PriceData extends CustomDateTimeFormat{
  val testPrice1: Price = Price(Some(1L), 1L, 100,  parseDateTimeString("2020-12-25 11:11:11"))
  val testPrice2: Price = Price(Some(2L), 2L, 30, parseDateTimeString("2021-01-21 11:11:11"))
  val testPrice3: Price = Price(None, 1L, 30, parseDateTimeString("2021-03-13 11:11:11"))

  val testPrices = Seq(testPrice1, testPrice2, testPrice3)
}
