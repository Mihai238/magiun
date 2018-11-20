package at.magiun.core.feature

/**
  * @param uniqueValues no more than 100 unique values
  * @param valueTypes type of values e.g. StringValue, BooleanValue, GenderValue
  */
case class ColumnMetaData(valueTypes: Set[String], missingValues: Int, uniqueValues: Long = 0) {

  private val maxUniqueValues = 100

  def combine(other: ColumnMetaData): ColumnMetaData = {
    val combinedValueTypes = if (valueTypes.isEmpty) {
      other.valueTypes
    } else if (other.valueTypes.isEmpty) {
      valueTypes
    } else {
      valueTypes.intersect(other.valueTypes)
    }

    ColumnMetaData(combinedValueTypes, missingValues + other.missingValues)
  }
}
