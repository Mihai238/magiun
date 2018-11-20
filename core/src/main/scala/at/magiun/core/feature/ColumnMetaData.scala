package at.magiun.core.feature

/**
  * @param valueTypes type of values e.g. StringValue, BooleanValue, GenderValue
  */
case class ColumnMetaData(valueTypes: Set[String],
                          missingValues: Int,
                          uniqueValues: Long = 0,
                          stats: SummaryStatistics = null,
                          distributions: Set[String] = Set()) {

  def combine(other: ColumnMetaData): ColumnMetaData = {
    val intersectedValueTypes = if (valueTypes.isEmpty) {
      other.valueTypes
    } else if (other.valueTypes.isEmpty) {
      valueTypes
    } else {
      valueTypes.intersect(other.valueTypes)
    }

    ColumnMetaData(intersectedValueTypes, missingValues + other.missingValues)
  }
}

case class SummaryStatistics(count: Long,
                             mean: Option[Double],
                             stddev: Option[Double],
                             min: Option[Double],
                             max: Option[Double],
                             median: Option[Double])