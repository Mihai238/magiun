package at.magiun.core.model

object SourceType extends Enumeration {
  val FileCsv: SourceType.Value = Value("FileCsv")
  val Mongo: SourceType.Value = Value("Mongo")
}
