package at.magiun.core.feature

import at.magiun.core.model.data.Distribution

case class ColumnMetaData(basicMeta: BasicMeta,
                          uniqueValues: Long = 0,
                          stats: SummaryStatistics = null,
                          distributions: Set[Distribution] = Set()) {

  def missingValues: Int = basicMeta.missingValues

  def intersectedValueTypes: Set[String] = basicMeta.intersectedValueTypes

  def unionValueTypes: Set[String] = basicMeta.unionValueTypes

}

/**
  * @param intersectedValueTypes type of values e.g. StringValue, BooleanValue, GenderValue
  */
case class BasicMeta(intersectedValueTypes: Set[String],
                     unionValueTypes: Set[String],
                     missingValues: Int) {

  def combine(other: BasicMeta): BasicMeta = {
    val newIntersected = if (intersectedValueTypes.isEmpty) {
      other.intersectedValueTypes
    } else if (other.intersectedValueTypes.isEmpty) {
      intersectedValueTypes
    } else {
      intersectedValueTypes.intersect(other.intersectedValueTypes)
    }

    val newUnion = if (unionValueTypes.isEmpty) {
      other.unionValueTypes
    } else if (other.unionValueTypes.isEmpty) {
      unionValueTypes
    } else {
      unionValueTypes.union(other.unionValueTypes)
    }

    BasicMeta(newIntersected, newUnion, missingValues + other.missingValues)
  }

}

case class SummaryStatistics(count: Long,
                             mean: Option[Double],
                             stddev: Option[Double],
                             min: Option[Double],
                             max: Option[Double],
                             median: Option[Double])