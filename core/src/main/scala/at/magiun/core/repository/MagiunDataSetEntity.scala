package at.magiun.core.repository

import at.magiun.core.model.SourceType

case class MagiunDataSetEntity(
                     id: Long,
                     name: String,
                     sourceType: SourceType.Value,
                     url: String
                   ) {

}


