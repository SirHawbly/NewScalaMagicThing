package FileIO

import java.io.{File, PrintWriter}

import scala.Array.concat
import scala.io.{BufferedSource, Source}

// ----------------------------------------------------------------------------

class CsvEntry (val _k: String, _v: String)
{
  val key: String = _k
  val value: String = _v

  override def toString: String =
    new String("[" + key + ",,," + value + "]")

  def toArray: Array[CsvEntry] =
    Array[CsvEntry](this)
}

// ----------------------------------------------------------------------------

class CsvParse {

  def parseInfile (inFilename:String,
                   outFilename:String): Unit =
  {
    var first: Boolean = true
    val infile: BufferedSource = Source.fromFile(inFilename)
    val writer: PrintWriter = new PrintWriter(new File(outFilename))
    var returnArray: Array[CsvEntry] = Array()

    for (line <- infile.getLines()) {

      if (first) { first = false }

      else {

        var tempItem: Array[CsvEntry] = Array()
        println(line)

        // ['a===aaa',,, 'b===bbb',,, ...]
        val pairs: Array[String] = line.toString.split(",,,").map(_.trim)

        for (pair <- pairs) {

          // ['a===aaa']
          val kv: Array[String] = pair.split("===")
          val key: String = if (kv.length == 1) new String(kv(0)) else new String(kv(0))
          val value: String = if (kv.length == 1) new String("---") else new String (kv(1))

          val entry: CsvEntry = new CsvEntry(key, value)

          tempItem = concat (tempItem, Array[CsvEntry](entry))
        }

        returnArray = concat (returnArray, tempItem)

      }
    }

    for (entry <- returnArray) {
      writer.write(entry.toString + "\n")
    }

    infile.close()
    writer.close()
  }

  def compileDeck (): Unit = {}
}

// ----------------------------------------------------------------------------

object CsvParseObj
{
  def main(args: Array[String]): Unit =
  {
    val inFilename: String = "parsed_cards.csv"
    val outFilename: String = "parsed_by_scala.csv"
    val testParser: CsvParse = new CsvParse

    testParser.parseInfile(inFilename, outFilename)
  }
}

// ----------------------------------------------------------------------------
