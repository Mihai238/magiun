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

class OrRestriction(r1: Restriction, r2: Restriction) extends Restriction {
  override def check(value: Any): Boolean = r1.check(value) || r2.check(value)
}

class AndRestriction(restrictions: List[Restriction]) extends Restriction {
  override def check(value: Any): Boolean = restrictions.forall(_.check(value))
}
