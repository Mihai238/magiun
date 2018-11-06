package at.magiun.core.model


case class MagiunDataSet(
                        id: String,
                        name: String,
                        dataSetSource: DataSetSource,
                        schema: Option[Schema]
                        ) {

}

case class DataSetSource(
                   sourceType: SourceType,
                   url: String
                 )