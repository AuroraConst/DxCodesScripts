//> using dep com.github.librepdf:openpdf:2.2.4
//> using dep dev.zio::zio:2.1.20

package pdfservice

import java.io.FileOutputStream
import com.lowagie.text.{Document, DocumentException}
import com.lowagie.text.pdf.{PdfContentByte, PdfWriter}
import zio._

// Service Definition
trait PdfService {
  def addText(text: String, x: Float, y: Float): Task[Unit]
  def drawLine(x1: Float, y1: Float, x2: Float, y2: Float): Task[Unit]
  def newPage(): Task[Unit]
}

// Live Implementation
final case class PdfServiceLive(
  document: Document,
  writer: PdfWriter,
  currentCanvas: Ref[Option[PdfContentByte]]
) extends PdfService {

  private def withCanvas(action: PdfContentByte => Task[Unit]): Task[Unit] =
    currentCanvas.get.flatMap {
      case Some(canvas) => action(canvas)
      case None          => ZIO.fail(new IllegalStateException("No active page"))
    }

  def addText(text: String, x: Float, y: Float): Task[Unit] =
    withCanvas { canvas =>
      ZIO.attempt(canvas.beginText())
        *> ZIO.attempt(canvas.setTextMatrix(x, y))
        *> ZIO.attempt(canvas.showText(text))
        *> ZIO.attempt(canvas.endText())
    }

  def drawLine(x1: Float, y1: Float, x2: Float, y2: Float): Task[Unit] =
    withCanvas { canvas =>
      ZIO.attempt {
        canvas.moveTo(x1, y1)
        canvas.lineTo(x2, y2)
        canvas.stroke()
      }
    }

  def newPage(): Task[Unit] =
    ZIO.attempt(document.newPage()).flatMap { _ =>
      ZIO.attempt(writer.getDirectContent)
        .flatMap(canvas => currentCanvas.set(Some(canvas)))
    }
}

object PdfService {
  val live: ZLayer[Scope, Throwable, PdfService] = ZLayer.scoped {
    for {
      doc     <- ZIO.acquireRelease(ZIO.attempt(new Document()))(d => ZIO.succeed(d.close()))
      os      <- ZIO.acquireRelease(ZIO.attempt(new FileOutputStream("output.pdf"))){f => ZIO.succeed(f.close())}
      writer  <- ZIO.attempt(PdfWriter.getInstance(doc, os))
      _       <- ZIO.attempt(doc.open())
      ref     <- Ref.make(Option.empty[PdfContentByte])
      service  = PdfServiceLive(doc, writer, ref)
      // Initialize first page
      _       <- service.newPage()
    } yield service
  }
}
