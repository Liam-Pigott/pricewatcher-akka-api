package mapper

import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import spray.json.{JsString, JsValue, RootJsonFormat}
import slick.jdbc.MySQLProfile.api._

import java.sql.Timestamp
import scala.util.Try

trait CustomDateTimeFormat {

  // allow multiple date formats over api
  private val parser1: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
  private val parser2: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd")

  // spray needs custom parser for date time
  implicit object DateJsonFormat extends RootJsonFormat[DateTime] {
    override def write(dt: DateTime): JsString = JsString(dateToString(dt))

    override def read(json: JsValue): DateTime = json match {
      case JsString(s) => parseDateTimeString(s)
      case _ => throw new Exception("Malformed datetime")
    }
  }

    def dateToString(dt: DateTime): String = {
      val parser1Try = Try(parser1.print(dt))
      val parser2Try = Try(parser2.print(dt))
      parser1Try.getOrElse(parser2Try.get)
    }

    def parseDateTimeString(s: String): DateTime = {
      val parser1Try = Try(parser1.parseDateTime(s))
      val parser2Try = Try(parser2.parseDateTime(s))
      parser1Try.getOrElse(parser2Try.get)
    }

  // needed for slick to be able to query based on date time
  implicit def customTimeMapping: BaseColumnType[DateTime] = MappedColumnType.base[DateTime, Timestamp](
    dateTime => new Timestamp(dateTime.getMillis),
    timeStamp => new DateTime(timeStamp.getTime)
  )
}


