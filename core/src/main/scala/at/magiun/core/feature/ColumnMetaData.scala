package at.magiun.core.feature

import at.magiun.core.model.data.Distribution

/**
  * @param intersectedValueTypes type of values e.g. StringValue, BooleanValue, GenderValue
  */
case class ColumnMetaData(intersectedValueTypes: Set[String],
                          unionValueTypes: Set[String],
                          missingValues: Int,
                          uniqueValues: Long = 0,
                          stats: SummaryStatistics = null,
                          distributions: Set[Distribution] = Set()) {

  def combine(other: ColumnMetaData): ColumnMetaData = {
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

    ColumnMetaData(newIntersected, newUnion, missingValues + other.missingValues)
  }
}

case class SummaryStatistics(count: Long,
                             mean: Option[Double],
                             stddev: Option[Double],
                             min: Option[Double],
                             max: Option[Double],
                             median: Option[Double])