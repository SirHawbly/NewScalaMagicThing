package Card

// ----------------------------------------------------------------------------

sealed abstract class color (_id:Char, _ic:Boolean, _nm:String) {
  val id : Char = _id
  val isColor : Boolean = _ic
  val name : String = _nm
}

case object red extends color ('R', true, "Red")
case object blue extends color ('U', true, "Blue")
case object green extends color ('G', true, "Green")
case object white extends color ('W', true, "White")
case object black extends color ('B', true, "Black")
case object colorless extends color ('C', false, "Colorless")
//val colors : List[color] = List(red,blue,green,white,black)

// ----------------------------------------------------------------------------

class Ability
{
  val trigger : String = "enter the battlefield"
  val keyword : String = "no keyword"
  val manaCost : String = "{2}{U}" // "no cost"
  val speed : String = "Sorcery"
}

// ----------------------------------------------------------------------------

class Counters
{
  val name : String = "+1 / +1"
  val amount : Int = 0
  val description : String = "changes power and toughness"
  val effect : List[Ability] = List()

  def trigger : Boolean = {
    if (amount > 10) {
      true
    } else {
      false
    }
  }

}

// ----------------------------------------------------------------------------

sealed abstract class BaseCard ()
{
  val cardOwner : String = "no owner"
  val currentOwner : String = "no owner"
  val currentState : String = "no state"
  val cardName : String = "no name"
  val manaCost : String = "{5}{W}{B/R}{2/G}" // "no cost"
  val colors : List[color] = List(red,blue,green,white,black)
  var colorID : List[color] = getColorID
  val convertedCost : Int = getCMC(manaCost)
  val cardType : List[String] = List("Base")
  var abilities : List[String] = List("asdf","asdf")
  val speed : String = "Sorcery"
  val tapped : Boolean = false

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

  // trys to atoi a string, if it can itll return
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
  def getColorID : List[color] =
  {
    var cs : List[color] = List()

    for (c <- colors)
    {
      if (manaCost.contains(c.id))
        cs :+= c
    }

    if (cs.isEmpty)
    {
      cs :+= colorless
    }

    cs
  }
}

// ----------------------------------------------------------------------------

case class Land ()
  extends BaseCard ()
{
  override def print()
  {
    super.print()
    println()
  }
}


// ----------------------------------------------------------------------------

case class Sorcery ()
  extends BaseCard ()
{
  override def print()
  {
    super.print()
    println()
  }
}

// ----------------------------------------------------------------------------

case class Instant ()
  extends BaseCard ()
{
  override val speed : String = "Instant"
  override def print()
  {
    super.print()
    println()
  }
}

// ----------------------------------------------------------------------------

case class Enchantment ()
  extends BaseCard ()
{
  override def print()
  {
    super.print()
    println()
  }
}

// ----------------------------------------------------------------------------

case class Creature ()
  extends BaseCard ()
{
  // override val abilities = List("Flashback: {2}{R}", "Deal 2 damage to target creature")
  val counters : List[Counters] = List()
  val power : Int = 0
  val tough : Int = 0

  override def print()
  {
    super.print()
    println(power + " / " + tough)
    println("--")
    println()
  }
}

// ----------------------------------------------------------------------------

case class Planeswalker ()
  extends BaseCard ()
{
  override def print()
  {
    super.print()
    println()
  }
}

// ----------------------------------------------------------------------------

case class Artifact ()
  extends BaseCard ()
{
  override def print()
  {
    super.print()
    println()
  }
}

// ----------------------------------------------------------------------------

case class Saga ()
  extends BaseCard ()
{
  var counter : Int = 1

  override def print()
  {
    super.print()
    println()
  }
}


// ----------------------------------------------------------------------------

object CardObj
{
  def main(args: Array[String]): Unit = {

    val x = Sorcery()
    x.print()

    val y = Creature()
    y.print()
  }
}