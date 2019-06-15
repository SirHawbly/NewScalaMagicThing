import Card.BaseCard

import scala.io.{BufferedSource, Source}
import scala.util.control.Breaks.{break, breakable}

object Deck
{
  def loadDeckList(file: String): List[List[String]] =
  {
    val infile: BufferedSource = Source.fromFile(file)
    var cardList: List[List[String]] = List()

    // go through the file pulling all lines
    for (line <- infile.getLines()) {
      breakable {
        val firstInd: Int = line.indexOf("x ")

        // if the index is 0 or negative, skip it
        if (firstInd < 1)
        {
          break
        }

        // else we can split the line and add it
        else
        {
          val count: String = line.substring(0, firstInd).toInt.toString

          val name: String = if (line.length > firstInd + 2)
            line.substring(firstInd + 2, line.length) else ""

          // [[c,n], [c,n], ..] ++ [c,n]
          val l: List[String] = List(count, name)
          cardList = cardList ++ List(l)
        }
      }
    }

    // cardList.foreach({x:List[String] => println(x)})
    cardList
  }

  def getCardList(_cl: List[List[String]]): List[String] =
  {
    var list: List[String] = List()

    for (item <- _cl)
    {
      for (i <- 0 until item.head.toInt)
      {
        list = list :+ item.tail.toString()
      }
    }

    // list.foreach({println(_)})
    // println(list)
    list
  }

  def generateCards(): List[BaseCard] =
  {
    /*
    for (item <- list)
    {
      cards = cards :+ new Sorcery(item.toString)
    }

    cards.foreach((x:BaseCard) => println(x.cardName, x.getCardType(x)))
    cards
     */

    List ()
  }
}

class Deck (val _cl: List[List[String]],
            val _cs: List[String])
{
  val cardList: List[List[String]] = _cl
  val cards: List[String] = _cs

  def this (_cl: List[List[String]]) =
    this (_cl, Deck.getCardList(_cl))

  def this (_f: String) =
    this(Deck.loadDeckList(_f))
}