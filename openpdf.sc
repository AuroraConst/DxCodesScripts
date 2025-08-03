//> using dep com.github.pathikrit::better-files:3.9.2
//> using dep com.github.librepdf:openpdf:2.2.4
import com.lowagie.text.pdf.PdfContentByte
import com.lowagie.text.Paragraph
import java.io.FileOutputStream

import java.nio.charset.Charset
import scala.sys.process._

import better.files._
import File._
import java.io.{File => JFile}
import com.lowagie.text.{Font,Phrase,Chunk, Element}
import com.lowagie.text.pdf.{PdfReader,PdfStamper,BaseFont,ColumnText};
import com.lowagie.text.pdf.parser.PdfTextExtractor;

/**
 * TODO I need to use simulation to determine where text gets cut off in a ColumnText so that the remainder can be put in
 * subsequent pages
*/
val formdir = root / "users" / "public" / "documents" / "forms"
val ordersPdf = formdir / "orders.pdf"
val stamperPdf = formdir / "filled_orders.pdf"
// val file = root/ "users" / "public" / "documents" / "forms" / "orders.fdf"
val adobepath = "\"C:\\Program Files\\Adobe\\Acrobat DC\\Acrobat\\Acrobat.exe\""


val pdfReader = new PdfReader(ordersPdf.toJava.getAbsolutePath())
// 2. Create a stamper to modify the PDF
val stamper:PdfStamper = new PdfStamper(pdfReader, new FileOutputStream(stamperPdf.toJava.getAbsolutePath()));
given   canvas1:PdfContentByte = stamper.getOverContent(1);

// 4. Create font and text
val bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
val bold = new Font(Font.HELVETICA, 12, Font.BOLD);
val boldUnderline = new Font(Font.HELVETICA, 12, Font.UNDERLINE | Font.BOLD);
val italic = new Font(Font.HELVETICA, 14, Font.ITALIC);

trait Rect {
  val x: Int
  val y: Int
  val width: Int
  val height: Int
  val canvas = canvas1
  def draw() = 
    canvas1.rectangle(x, y, width, height)
    canvas1.stroke();
}

case class LineSummary() extends Rect:
  val x = 12
  val y = 580
  val width = 200
  val height = 80

  lazy val columnText = new ColumnText(canvas);

  def writeText(text: String): Unit = {
    columnText.setSimpleColumn(
      x + 4, // x position (from left)
      y + 4, // y position (from bottom)
      x + width - 4, // width
      y + height - 4 // height
    )
    canvas.beginText();
    canvas.setFontAndSize(bf, 12);
    columnText.addElement(new Phrase("Line Summary: ", new Font(Font.HELVETICA, 12)));
    columnText.go()
  }

case class Hx() extends Rect:
  val x = 332
  val y = 40
  val width = 270
  val height = 500

  lazy val columnText = new ColumnText(canvas);


  def writeText(text: String): Unit = {
    columnText.setSimpleColumn(
      x + 4, // x position (from left)
      y + 4, // y position (from bottom)
      x + width - 4, // width
      y + height - 4 // height
    )
    canvas.beginText();
    canvas.setFontAndSize(bf, 8);
    val pg1 = new Paragraph("hello world", new Font(Font.HELVETICA, 8));
    val pg2 = new Paragraph(text, new Font(Font.HELVETICA, 8));
    val pg3 = new Paragraph("", new Font(Font.HELVETICA, 8));


    pg3.add(new Chunk("This is ", new Font(Font.HELVETICA, 8)));
    pg3.add(new Chunk("bold", bold));
    pg3.add(new Chunk("bold underline", boldUnderline));
    pg3.add(new Chunk(" and ", new Font(Font.HELVETICA, 14)));
    pg3.add(new Chunk("italic", italic));

    columnText.addElement(pg1)   
    columnText.addElement(pg2)
    columnText.addElement(pg3)
    val lines = columnText.getLinesWritten()

    columnText.go()
  }

val hx = Hx()
hx.draw()
hx.writeText("comes just from things like you can't get on the housing ladder. " +
  "I think it comes from a real sense that you can't build anything stable, you can't attach to anything or anyone or anywhere. There's no real sense of community, there's no real sense of meaning and I think sometimes that actually affects people in more wealthy households more so because they really don't have that sense of meaning and I think what you said is true that you know we're humans we need more than material goods and consumption but I would argue a lot of the distress I've seen is among wealthy affluent people who just feel hopeless in their lives.")

val lineSummary = LineSummary()
lineSummary.draw()
// lineSummary.writeText("This is a line summary that will be written in the rectangle defined by LineSummary.")






val fields = stamper.getAcroFields()
import collection.JavaConverters._
stamper.getAcroFields().getAllFields().asScala.foreach(
  (name,value) => {
    println(s"Field name: ${name}, value: ${value}")
  }
)

fields.setField("allergies","Hello World")

// println(pdfReader.getNumberOfPages())


given Charset = Charset.forName("UTF-8")


def showOrders (): Unit = {
  s"$adobepath ${ordersPdf.toJava.getAbsolutePath()}".! 
}

def showStampedOrders (): Unit = {
  s"$adobepath ${stamperPdf.toJava.getAbsolutePath()}".! 
}
stamper.close() // Save the changes to the PDF
pdfReader.close() // Close the PDF reader

showStampedOrders()

// s"$adobepath ${file.path.toString().replace("\\","\\\\")}".! //there is a bug thatputs an extra back slash in the path to Orders.fdf