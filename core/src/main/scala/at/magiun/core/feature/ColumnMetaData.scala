package at.magiun.core.feature

/**
  * @param uniqueValues no more than 100 unique values
  * @param valueTypes type of values e.g. StringValue, BooleanValue, GenderValue
  */
case class ColumnMetaData(uniqueValues: Set[String], valueTypes: Set[String], missingValues: Int) {

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

    ColumnMetaData(combinedUniqueValues, combinedValueTypes, missingValues + other.missingValues)
  }
}
