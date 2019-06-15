/*
 * Copyright (c) 2019 Christopher Bartlett
 * [This program is licensed under the "MIT License"]
 * Please see the file LICENSE in the source
 * distribution of this software for license terms.
 */

package TempFiles

import Array.concat
// import java.security.KeyStore.TrustedCertificateEntry

import scala.io.{BufferedSource, Source}
import java.io.{File, PrintWriter}

class Snippets {

  // match example
  def search (a:Any):Any = a match {
    case 1  => println("One")
    case "Two" => println("Two")
    case "Hello" => println("Hello")
    case _ => println("No")
  }

  // variable arguments
  def sum(args: Int*): Int = {
    var sum = 0
    for(a <- args) sum+=a
    sum
  }

  // anonomous functions
  def anon() {
    var result1 = (a:Int, b:Int) => a+b        // Anonymous function by using => (rocket)
    var result2 = (_:Int)+(_:Int)              // Anonymous function by using _ (underscore) wild card
    println(result1(10,10))
    println(result2(10,10))
  }

  // nesting
  def add(a:Int, b:Int, c:Int): Int = {
    def add2(x:Int,y:Int) = {
      x+y
    }
    add2(a,add2(b,c))
  }

  // shows anon functions
  def anonForeach(): Unit = {
    var sum = 0
    val x : List[Int] = List(1,2,3,4,5)
    x.foreach{sum += _}
  }
  val manas: List[String] =
    List("","{5}","{G}","{10}{X}{1}{U}{R}",
      "{G}{G}{R}","{10}{W}{B}{11}")

  // tries to atoi a string, if it can it'll return
  // that value, if not returns -1.
  def toInt(s: String): Int =
  {
    try {
      s.toInt
    } catch {
      case e: Exception => -1
    }
  }

  // split the given string into pieces, based off
  // the curly braces, add the costs together, with
  // X == "" = 0 cmc.
  def getCMC (c : String) : Int =
  {
    var sum: Int = 0
    val costs : Array[String] = c.split("\\{")

    if (c == "") return 0

    for (i <- costs)
    {
      val temp = i.split("\\}")(0)

      if ((temp != "") && (temp != "X"))
      {
        if (toInt(temp) != -1) {
          sum += toInt(temp)
        } else {
          sum += 1
        }
      }
    }

    sum
  }

  manas.foreach{ x => println("'"+x+"'"); println(getCMC(x)) }

}

class csvEntry (val _k : String, _v : String) {
  val key : String = _k
  val value : String = _v

  override def toString : String = {
    new String("[" + key + ",,," + value + "]")
  }

  def toArray : Array[csvEntry] = {
    Array[csvEntry](this)
  }
}


object SnippetsObj { // TODO - move to FileIO.scala

  def main (args : Array[String]) : Unit =
  {
    val inFilename : String = "parsed_cards.csv"
    val outFilename : String = "parsed_by_scala.csv"
    var first : Boolean = true
    val infile : BufferedSource = Source.fromFile(inFilename)
    val writer = new PrintWriter(new File(outFilename))
    var returnArray : Array[csvEntry] = Array()

    for (line <- infile.getLines())
    {

      if (first) { first = false }

      else {

        var tempItem : Array[csvEntry] = Array()
        println(line)

        // ['a===aaa',,, 'b===bbb',,, ...]
        val pairs: Array[String] = line.toString.split(",,,").map(_.trim)

        for (pair <- pairs) {

          // ['a===aaa']
          val kv: Array[String] = pair.split("===")

          /*
          val keyPre : String = if (kv(0).apply(0) == '\'' || kv(0).apply(0) == '[') "" else "'"
          val valPre : String = if (kv(1).apply(0) == '\'' || kv(1).apply(0) == '[') "" else "'"
          val keyPost : String = if (kv(0).last == '\'' || kv(0).last == ']') "" else "'"
          val valPost : String = if (kv(1).last == '\'' || kv(1).last == ']') "" else "'"
          */

          // println(pair.toString)

          val key : String = if (kv.length == 1) new String(kv(0)) else new String(kv(0))
          val value: String = if (kv.length == 1) new String("---") else new String (kv(1))

          val entry: csvEntry = new csvEntry(key, value)

          // println(key + "===" + entry)
          tempItem = concat (tempItem, Array [csvEntry] (entry))
        }

        // for (pair <- tempItem) { println(pair.toString) }

        returnArray = concat (returnArray, tempItem)

      }
    }

    // println(returnArray.deep.mkString(" "))
    for (entry <- returnArray) {
      writer.write(entry.toString + "\n")
    }

    infile.close()
    writer.close()
  }
}