package examples

import zio._
import pdfservice.PdfService

object PdfServiceExample extends ZIOAppDefault {

  // Example program that uses PdfService
  val pdfProgram: ZIO[PdfService, Throwable, Unit] = for {
    service <- ZIO.service[PdfService]
    
    // Add some text to the first page
    _ <- service.addText("Hello World!", 100f, 700f)
    _ <- service.addText("This is a ZIO PDF example", 100f, 680f)
    
    // Draw a line
    _ <- service.drawLine(100f, 650f, 400f, 650f)
    
    // Create a new page
    _ <- service.newPage()
    
    // Add content to the second page
    _ <- service.addText("Page 2", 100f, 700f)
    _ <- service.drawLine(50f, 680f, 500f, 680f)
    
    _ <- Console.printLine("PDF created successfully!")
  } yield ()

  // Run the program with the PdfService layer
  def run: ZIO[Any, Any, Any] = 
    pdfProgram.provide(
      PdfService.live,
      Scope.default
    )
}
