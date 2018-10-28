package at.magiun.core.feature

case class ColumnMetaData(uniqueValues: Set[String], valueTypes: Set[String]) {

  private val maxUniqueValues = 100

  def combine(other: ColumnMetaData): ColumnMetaData = {
    val combinedValueTypes = valueTypes ++ other.valueTypes

    val combinedUniqueValues =
      if (uniqueValues.size < maxUniqueValues && other.uniqueValues.size < maxUniqueValues) {
        uniqueValues ++ other.uniqueValues
      } else if (uniqueValues.size >= maxUniqueValues) {
        uniqueValues
      } else {
        other.uniqueValues
      }

    ColumnMetaData(combinedUniqueValues, combinedValueTypes)
  }
}
