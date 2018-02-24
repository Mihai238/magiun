package at.magiun.core.repository

import at.magiun.core.model.SourceType

final case class MagiunDataSetEntity(
                     id: Long = 0L,
                     name: String,
                     sourceType: String,
                     url: String
                   ) {

}


