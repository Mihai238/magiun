package at.magiun.core.model


case class MagiunDataSet(
                        id: Long,
                        name: String,
                        sourceType: SourceType.Value,
                        url: String,
                        schema: Schema
                        ) {

}
