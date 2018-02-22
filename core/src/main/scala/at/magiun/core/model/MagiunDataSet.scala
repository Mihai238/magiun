package at.magiun.core.model


case class MagiunDataSet(
                        id: Long,
                        name: String,
                        sourceType: SourceType,
                        url: String,
                        schema: Schema
                        ) {

}
