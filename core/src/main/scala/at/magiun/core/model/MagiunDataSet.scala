package at.magiun.core.model


case class MagiunDataSet(
                        id: Long,
                        name: String,
                        dataSetSource: DataSetSource,
                        schema: Schema
                        ) {

}

case class DataSetSource(
                   sourceType: SourceType,
                   url: String
                 )