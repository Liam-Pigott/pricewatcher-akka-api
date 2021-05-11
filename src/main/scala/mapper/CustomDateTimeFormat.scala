package mapper

import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import spray.json.{JsString, JsValue, RootJsonFormat}

object CustomDateTimeFormat {

  private val parser: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-mm-dd hh:mm:ss")

  // spray needs custom parser for date time
  implicit object DateJsonFormat extends RootJsonFormat[DateTime] {
    override def write(dt: DateTime): JsString = JsString(dateToString(dt))

    override def read(json: JsValue): DateTime = json match {
      case JsString(s) => parseDateTimeString(s)
      case _ => throw new Exception("Malformed datetime")
    }
  }

  def dateToString(dt: DateTime): String =
    parser.print(dt)

  def parseDateTimeString(s: String): DateTime =
    parser.parseDateTime(s)
}
