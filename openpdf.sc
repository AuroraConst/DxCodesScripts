//> using dep com.github.pathikrit::better-files:3.9.2
//> using dep com.github.librepdf:openpdf:2.2.4
import java.io.FileOutputStream

import java.nio.charset.Charset
import scala.sys.process._

import better.files._
import File._
import java.io.{File => JFile}
import com.lowagie.text.{Font,Phrase,Chunk, Element}
import com.lowagie.text.pdf.{PdfReader,PdfStamper,BaseFont,ColumnText};
import com.lowagie.text.pdf.parser.PdfTextExtractor;

val formdir = root / "users" / "public" / "documents" / "forms"
val ordersPdf = formdir / "orders.pdf"
val stamperPdf = formdir / "filled_orders.pdf"
// val file = root/ "users" / "public" / "documents" / "forms" / "orders.fdf"
val adobepath = "\"C:\\Program Files\\Adobe\\Acrobat DC\\Acrobat\\Acrobat.exe\""


val pdfReader = new PdfReader(ordersPdf.toJava.getAbsolutePath())
// 2. Create a stamper to modify the PDF
val stamper:PdfStamper = new PdfStamper(pdfReader, new FileOutputStream(stamperPdf.toJava.getAbsolutePath()));
val  canvas = stamper.getOverContent(1);

// 4. Create font and text
val bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
val bold = new Font(Font.HELVETICA, 12, Font.BOLD);
val italic = new Font(Font.HELVETICA, 14, Font.ITALIC);


           
// 5. Absolute positioning (x,y coordinates in points)
canvas.beginText();
canvas.setFontAndSize(bf, 12);
canvas.setTextMatrix(100, 700); // 100pt from left, 700pt from bottom
canvas.showText("This text is absolutely positioned");

val  phrase = new Phrase();
phrase.add(new Chunk("This is ", new Font(Font.HELVETICA, 12)));
phrase.add(new Chunk("bold", bold));
phrase.add(new Chunk(" and ", new Font(Font.HELVETICA, 14)));
phrase.add(new Chunk("italic", italic));
// Position using ColumnText
ColumnText.showTextAligned(
    canvas,
    Element.ALIGN_LEFT,
    phrase,
    50, // x position (from left)
    500, // y position (from bottom)
    0    // rotation angle
);


canvas.endText();
canvas.setRGBColorStroke(255, 0, 0)
canvas.rectangle( 100, 700, 200, 20); // Draw a rectangle around the text
canvas.stroke();



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