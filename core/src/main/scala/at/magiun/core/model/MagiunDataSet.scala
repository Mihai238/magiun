package at.magiun.core.model


case class MagiunDataSet(
                     id: Long,
                     name: String,
                     sourceType: SourceType.Value,
                     url: String
                   ) {

}

object SourceType extends Enumeration {
  val FileCsv: SourceType.Value = Value("FileCsv")
  val Mongo: SourceType.Value = Value("Mongo")
}
