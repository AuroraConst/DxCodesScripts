//> using file "project.scala"

import scala.io.Source
import scala.util.{Using, Try}
import scala.collection.mutable

case class DiagnosticCode(code: String, label: String)
val dxCodeMap =  mutable.Map[String, Seq[String]]()


def readDiagnosticCodes(filename: String): Seq[DiagnosticCode] = {
  Using(Source.fromFile(filename)) { source =>
    val lines = source.getLines().toList
    
    // Process lines in pairs (code, label)
    lines.grouped(2).collect {
      case List(code, label) => DiagnosticCode(code.trim, label.trim)
      case List(code) => DiagnosticCode(code.trim, "") // Handle odd number of lines
    }.toSeq
  }.fold(
    error => {
      println(s"Error reading file: ${error.getMessage}")
      Seq.empty
    },
    identity
  )
}

// Read the diagnostic codes
val diagnosticCodes = readDiagnosticCodes("dxcodes.txt")

// Print some examples
println(s"Total diagnostic codes loaded: ${diagnosticCodes.length}")


diagnosticCodes.foreach(dc => 
  dxCodeMap.updateWith(dc.code) {
    case Some(existing) => Some(existing :+ dc.label)
    case None => Some(Seq(dc.label))
  }
)

println("summary of diagnostic codes with multiple labels:")
dxCodeMap.filter(_._2.size > 1).foreach { case (code, labels) =>
  println(s"Code: $code has multiple labels: ${labels.mkString(", ")}")
}

