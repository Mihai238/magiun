package at.magiun.core.feature

import java.util.regex.Pattern

sealed trait Restriction extends Serializable {
  def check(value: Any): Boolean
}

class NoOpRestriction extends Restriction {
  override def check(value: Any): Boolean = true
}

class PatternRestriction(pattern: String) extends Restriction {

  private val regex = Pattern.compile(pattern)

  override def check(value: Any): Boolean = regex.matcher(value.toString).matches()
}

class EqualsRestriction(refValue: String) extends Restriction {

  override def check(value: Any): Boolean = refValue == value.toString
}

class MinInclusiveRestriction(min: Double) extends Restriction {
  override def check(value: Any): Boolean = {
    value match {
      case v: Double => min <= v
      case v: Int => min <= v
      case v: String => try {
        min <= v.toDouble
      } catch {
        case _: Throwable => false
      }
      case _ => false
    }
  }
}

class MaxInclusiveRestriction(max: Double) extends Restriction {
  override def check(value: Any): Boolean = {
    value match {
      case v: Double => v <= max
      case v: Int => v <= max
      case v: String => try {
        v.toDouble <= max
      } catch {
        case _: Throwable => false
      }
      case _ => false
    }
  }
}

class DataTypeRestriction(requiredType: String) extends Restriction {
  override def check(value: Any): Boolean = {
    requiredType match {
      case "integer" =>
        value match {
          case _: Int => true
          case v: String => try {
            v.toInt
            true
          } catch {
            case _: Throwable => false
          }
          case _ => false
        }
      case "decimal" =>
        value match {
          case _: Int => true
          case _: Double => true
          case v: String => try {
            v.toDouble
            true
          } catch {
            case _: Throwable => false
          }
          case _ => false
        }
      case _ => false
    }
  }
}

//
// Composite restrictions
//

class NotRestriction(restriction: Restriction) extends Restriction {
  override def check(value: Any): Boolean = !restriction.check(value)
}

class OrRestriction(restrictions: List[Restriction]) extends Restriction {
  override def check(value: Any): Boolean = restrictions.exists(_.check(value))
}

class AndRestriction(restrictions: List[Restriction]) extends Restriction {
  override def check(value: Any): Boolean = restrictions.forall(_.check(value))
}
