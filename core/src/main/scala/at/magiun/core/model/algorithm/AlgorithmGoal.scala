package at.magiun.core.model.algorithm

import at.magiun.core.model.ontology.OntologyClass
import enumeratum._

import scala.collection.immutable

sealed abstract class AlgorithmGoal(val name: String, val ontologyClass: OntologyClass.Value) extends EnumEntry

object AlgorithmGoal extends Enum[AlgorithmGoal] with CirceEnum[AlgorithmGoal] {
  case object GoalRegression extends AlgorithmGoal("GoalRegression", OntologyClass.GoalRegression)
  case object GoalClassification extends AlgorithmGoal("GoalClassification", OntologyClass.GoalClassification)

  val values: immutable.IndexedSeq[AlgorithmGoal] = findValues


}
