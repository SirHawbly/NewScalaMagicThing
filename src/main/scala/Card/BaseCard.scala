/*
 * Copyright (c) 2019 Christopher Bartlett
 * [This program is licensed under the "MIT License"]
 * Please see the file LICENSE in the source
 * distribution of this software for license terms.
 */

package Card

// ----------------------------------------------------------------------------

sealed abstract class color (_id: Char, _ic: Boolean, _nm: String)
{
  val id: Char = _id
  val isColor: Boolean = _ic
  val name: String = _nm
}

// ----------------------------------------------------------------------------

object Colors
{
  case object red extends color ('R', true, "Red")
  case object blue extends color ('U', true, "Blue")
  case object green extends color ('G', true, "Green")
  case object white extends color ('W', true, "White")
  case object black extends color ('B', true, "Black")
  case object colorless extends color ('C', false, "Colorless")
  val colors: List[color] = List(red,blue,green,white,black)
}

// ----------------------------------------------------------------------------

class OwnerInfo (_co: String,
                 _cos: String,
                 _cus: String)
{
  val cardOwner: String = _co
  val currentOwner: String = _cos
  val currentState: String = _cus
  def this () =
    this ("no owner",
          "no owner",
          "no state")
}

// ----------------------------------------------------------------------------

class Ability (_tt: String,
               _k: String,
               _e: Int => String,
               _mc: String,
               _s: String)
{
  val triggerType: String = _tt
  val keyword: String = _k
  val effect: Int => String = _e
  val manaCost: String = _mc
  val speed: String = _s

  def this () =
    this ("No trigger", "No Keyword", x => if (x == 10) "ass" else "", "{}", "No Speed")
}

// ----------------------------------------------------------------------------

class Counters (_n: String,
                _a: Int,
                _d: String,
                _pr: List[Ability],
                _po: List[Ability],
                _t: Boolean => Boolean)
{
  val name: String = _n
  val amount: Int = _a
  val description: String = _d
  val preEffect: List[Ability] = _pr
  val postEffect: List[Ability] = _po

  def triggerFun: Boolean => Boolean = _t

  def this () =
    this ("None Name", 0, "None Desc", List(), List(), {x: Boolean => if (!x) x else !x})
}

// ----------------------------------------------------------------------------

object BaseCard {

  def makeCard (info: List[String]): BaseCard =
  {
    new Land()
  }
}

// ----------------------------------------------------------------------------

sealed abstract class BaseCard (_own: OwnerInfo,
                                _cn: String,
                                _mc: String,
                                _ct: List[String],
                                _a: List[String],
                                _s: String,
                                _t: Boolean)
{
  val ownership: OwnerInfo = _own
  val cardName: String = _cn
  val manaCost: String = _mc
  var colorID: List[color] = getColorID
  val convertedCost: Int = getCMC(manaCost)
  val cardType: List[String] = _ct
  var abilities: List[String] = _a
  val permanent: Boolean = true
  val speed: String = _s
  val tapped: Boolean = _t

  def this() =
    this(new OwnerInfo(), "No Name", "No Cost", List("None", "Type"), List("No", "Abilities"), "None Speed", false)

  def this(name: String) =
    this(new OwnerInfo(), name, "No Cost", List("None", "Type"), List("No", "Abilities"), "None Speed", false)

  def getCardType (card: BaseCard): String =
  {
    card match
    {
      case Land(_, _, _, _, _, _, _) => "Land"
      case Sorcery(_, _, _, _, _, _, _) => "Sorcery"
      case Instant(_, _, _, _, _, _, _) => "Instant"
      case Enchantment(_, _, _, _, _, _, _) => "Enchantment"
      case Creature(_, _, _, _, _, _, _, _, _, _) => "Creature"
      case Planeswalker(_, _, _, _, _, _, _, _) => "Planeswalker"
      case Artifact(_, _, _, _, _, _, _) => "Artifact"
      case Saga(_, _, _, _, _, _, _, _) => "Saga"
      case _ => "Base"
    }
  }

  // prints the name, cost, colors, abilities, and
  // types of the base card.
  def print()
  {
    println("--") // Name and Costs
    println(cardName + " : " + manaCost)
    colorID.foreach{ a => printf(a.name.toString + " ") }
    println("\nCMC: " + convertedCost + " mana")
    println("--") // Types
    cardType.foreach{ println }
    println("--") // Abilities
    if (abilities.nonEmpty)
    {
      abilities.foreach{ println }
      println("--")
    }
  }

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
  def getCMC (c: String): Int =
  {
    var sum: Int = 0
    val costs: Array[String] = c.split("\\{")

    if (c == "") return 0

    for (i <- costs)
    {
      val temp = i.split("/?\\}?")(0)

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

  // returns the names of the colors in the cost of
  // the card.
  def getColorID: List[color] =
  {
    var cs: List[color] = List()

    for (c <- Colors.colors)
    {
      if (manaCost.contains(c.id))
        cs :+= c
    }

    if (cs.isEmpty)
      cs :+= Colors.colorless

    cs
  }
}

// ----------------------------------------------------------------------------

case class Land (_own: OwnerInfo,
                 _cn: String,
                 _mc: String,
                 _ct: List[String],
                 _a: List[String],
                 _s: String,
                 _t: Boolean)
  extends BaseCard (_own,
                    _cn,
                    _mc,
                    _ct,
                    _a,
                    _s,
                    _t)
{

  def this () =
    this (new OwnerInfo, "", "", List(""), List(""), "", false)

  def this (name: String) =
    this (new OwnerInfo, name, "", List(""), List(""), "", false)

  override def print()
  {
    super.print()
    println()
  }
}

// ----------------------------------------------------------------------------

case class Sorcery (_own: OwnerInfo,
                    _cn: String,
                    _mc: String,
                    _ct: List[String],
                    _a: List[String],
                    _s: String,
                    _t: Boolean)
  extends BaseCard (_own,
                    _cn,
                    _mc,
                    _ct,
                    _a,
                    _s,
                    _t)
{

  override val permanent: Boolean = false

  def this () =
    this (new OwnerInfo(), "", "", List(""), List(""), "", false)

  def this (name: String) =
    this (new OwnerInfo(), name, "", List(""), List(""), "", false)

  override def print()
  {
    super.print()
    println()
  }
}

// ----------------------------------------------------------------------------

case class Instant (_own: OwnerInfo,
                    _cn: String,
                    _mc: String,
                    _ct: List[String],
                    _a: List[String],
                    _s: String,
                    _t: Boolean)
  extends BaseCard (_own,
                    _cn,
                    _mc,
                    _ct,
                    _a,
                    _s,
                    _t)
{
  override val permanent: Boolean = false
  override val speed: String = "Instant"

  def this () =
    this (new OwnerInfo(), "", "", List(""), List(""), "", false)

  def this (name: String) =
    this (new OwnerInfo(), name, "", List(""), List(""), "", false)

  override def print()
  {
    super.print()
    println()
  }
}

// ----------------------------------------------------------------------------

case class Enchantment (_own: OwnerInfo,
                        _cn: String,
                        _mc: String,
                        _ct: List[String],
                        _a: List[String],
                        _s: String,
                        _t: Boolean)
  extends BaseCard (_own,
                    _cn,
                    _mc,
                    _ct,
                    _a,
                    _s,
                    _t)
{
  def this () =
    this (new OwnerInfo(), "", "", List(""), List(""), "", false)

  def this (name: String) =
    this (new OwnerInfo(), name, "", List(""), List(""), "", false)

  override def print()
  {
    super.print()
    println()
  }
}

// ----------------------------------------------------------------------------

case class Creature (_own: OwnerInfo,
                     _cn: String,
                     _mc: String,
                     _ct: List[String],
                     _a: List[String],
                     _s: String,
                     _t: Boolean,
                     _c: List[Counters],
                     _p: Int,
                     _to: Int)
  extends BaseCard (_own,
                    _cn,
                    _mc,
                    _ct,
                    _a,
                    _s,
                    _t)
{
  // override val abilities = List("Flashback: {2}{R}", "Deal 2 damage to target creature")
  val counters: List[Counters] = _c
  val power: Int = _p
  val tough: Int = _to

  def this () =
    this (new OwnerInfo(), "", "", List(""), List(""), "", false, List(), 0, 0)

  def this (name: String) =
    this (new OwnerInfo(), name, "", List(""), List(""), "", false, List(), 0, 0)

  override def print()
  {
    super.print()
    println(power + " / " + tough)
    println("--")
    println()
  }
}

// ----------------------------------------------------------------------------

case class Planeswalker (_own: OwnerInfo,
                         _cn: String,
                         _mc: String,
                         _ct: List[String],
                         _a: List[String],
                         _s: String,
                         _t: Boolean,
                         _l: Int)
  extends BaseCard (_own,
                    _cn,
                    _mc,
                    _ct,
                    _a,
                    _s,
                    _t)
{
  val loyalty: Int = _l

  def this () =
    this (new OwnerInfo(), "", "", List(""), List(""), "", false, 0)

  def this (name: String) =
    this (new OwnerInfo(), name, "", List(""), List(""), "", false, 0)

  override def print()
  {
    super.print()
    println(loyalty)
    println("--")
    println()
  }
}

// ----------------------------------------------------------------------------

case class Artifact (_own: OwnerInfo,
                     _cn: String,
                     _mc: String,
                     _ct: List[String],
                     _a: List[String],
                     _s: String,
                     _t: Boolean)
  extends BaseCard (_own,
                    _cn,
                    _mc,
                    _ct,
                    _a,
                    _s,
                    _t)
{

  def this () =
    this (new OwnerInfo(), "", "", List(""), List(""), "", false)

  def this (name: String) =
    this (new OwnerInfo(), name, "", List(""), List(""), "", false)

  override def print()
  {
    super.print()
    println()
  }
}

// ----------------------------------------------------------------------------

case class Saga (_own: OwnerInfo,
                 _cn: String,
                 _mc: String,
                 _ct: List[String],
                 _a: List[String],
                 _s: String,
                 _t: Boolean,
                 _c: Int)
  extends BaseCard (_own,
                    _cn,
                    _mc,
                    _ct,
                    _a,
                    _s,
                    _t)
{
  var ageCounter : Int = _c

  def this () =
    this (new OwnerInfo(), "", "", List(""), List(""), "", false, 1)

  def this (name: String) =
    this (new OwnerInfo(), name, "", List(""), List(""), "", false, 1)

  override def print()
  {
    super.print()
    println(ageCounter)
    println("--")
    println()
  }
}


// ----------------------------------------------------------------------------

object CardObj
{
  def main(args: Array[String]): Unit = {

    val x = new Sorcery()
    x.print()

    val y = new Creature()
    y.print()
  }
}

// ----------------------------------------------------------------------------
