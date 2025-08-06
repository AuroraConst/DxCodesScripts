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
hx.writeText("""will be their next place. And they will not be deterred. What we need to do is
we need to recognize that if we want to stop the slide of democracy, we as
Democrats, we as people in the pro-democracy movement, need to tell them that if
they take five seats, we're going to put 30 seats on the table. Right? Because
they need to understand that there will not just be parity if things go against
them, but actually if they guess wrong and this winds up escalating, they could
wind up on the severe losing side. So when people say to me, you know, don't
bring a knife to a gunfight, I don't even want to bring a gun to a gunfight. You
know, I want to bring something more than a gun to the gunfight. I want to bring
missiles to the gunfight. I want to bring hand grenades to the gunfight because
I want the other side to understand that not only are we going to fight as hard
as them, but we are going to fight smarter than them. That's how we need a
fight. We need to show them that touching the stove means you get burned. In
fact, I spoke with the governors of California, Illinois, New York, and Maryland
and pushed them all to take on this fight. Here's what that looked like. We can
in essence, likely neuter the total number of districts that Trump is looking to
get with Abbott and rigging those maps here in the state of California. Hope
other governors consider doing similar things. You've got to fight fire with
fire. This is an existential moment. We have agency. We can act superior. We can
act holier than thou and watch the last half century wiped out in real time. I
think we need to be held to a higher level of accountability and meet this
moment and lean in on this and get tough. From your vantage in terms of what
you're able to do in Illinois, are you willing to fight fire with fire here and
respond in a commensurate way if indeed we see Greg Abbott engage in a partisan
gerrymander in Texas? Well, I appreciate the question. I'll begin my answer by
saying I've been on team fight since I first got elected in 2018. I really
believe we have to make choices right now about whether you're going to be on
team fight or team cave. And the Republicans are using every, frankly,
everything, whether it's cheating, breaking the law, you know, trying to do
things that I think are non-traditional. And we Democrats can't live by the old
rules. We've got to live by the rules, but we've also got to react and fight.
And so when I think about what they're trying to do in Texas, and by the way,
they're also trying to do it next door to me here in Missouri, next to Illinois,
and other places, I think, you know, historically that's, you know, midstream
redistricting was not something that we Democrats would do. But if they force
us, we've got to put everything on the table. We've got to make decisions that
maybe we wouldn't have made before. And so that's why I'm on the team here. I
think that governors across the country that have the ability, maybe it's the
ones that where we've got trifectas, we've got to do everything we can to stand
up to what Donald Trump and Greg Abbott are trying to do. I just, I'm not going
to countenance anymore the idea that we're going to sit around and watch them do
it and complain from the sidelines when we have the ability to stop them. And
so, I mean, you know, we're kind of in the world of hypotheticals, but the
reality is that the special legislative session was already called in Texas.
They are meeting right now in Texas. There's no indication, at least as of right
now, that they're stepping back. And so if Greg Abbott continues on exactly the
path that we're in right now, would it be fair to say, to the extent that you
can say definitively, that you would be willing to fight fire with fire and look
toward calling, I don't know what the process would be in Illinois, but calling
a legislative session to do exactly what they're doing in Texas and what Gavin
Newsom has said that he'd be willing to do in California? Yeah. Like I said,
everything's on the table. It's why I've spoken up about it. It's why I met with
the Texas legislators. You know, they walked in the door and I know that they
were here in Chicago to convince me, but before they opened their mouths, I
said, you had me at hello. I'll use every power I have to push back against this
blatant abuse of power. It has gone on too far, as you said. You know, we have a
strong advocacy group of good government people in our state. They want
independent redistricting. They want all this. It is, you know, it's an ideal
world. But when we have our combatants, the other side, you know, cheating,
rigging the system, like we can't sit on the sidelines. So I'm saying I will do
what I can, partially looking at our constitution, because unlike other states,
constitution, let me just say this. I think California does not""" )

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