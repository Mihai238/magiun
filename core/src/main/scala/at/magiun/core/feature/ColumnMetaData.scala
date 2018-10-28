package at.magiun.core.feature

case class ColumnMetaData(uniqueValues: Set[String], valueTypes: Set[String]) {

  private val maxUniqueValues = 100

  def combine(other: ColumnMetaData): ColumnMetaData = {
    val combinedValueTypes = if (valueTypes.isEmpty) {
      other.valueTypes
    } else if (other.valueTypes.isEmpty) {
      valueTypes
    } else {
      valueTypes.intersect(other.valueTypes)
    }

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
