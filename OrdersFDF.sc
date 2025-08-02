//> using dep com.github.pathikrit::better-files:3.9.2

// custom charset:
import java.nio.charset.Charset
import scala.sys.process._

import better.files._
import File._
import java.io.{File => JFile}

val file = root/ "users" / "public" / "documents" / "forms" / "orders.fdf"
val string = file.contentAsString(charset = Charset.forName("UTF-8")).replace("\\\\\\\\","\\\\")
file.write(string)

val adobepath = "\"C:\\Program Files\\Adobe\\Acrobat DC\\Acrobat\\Acrobat.exe\""

s"$adobepath ${file.path.toString().replace("\\","\\\\")}".! //there is a bug thatputs an extra back slash in the path to Orders.fdf