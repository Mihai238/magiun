package at.magiun.core.model

case class Schema(
                   columns: List[Column]
                 ) {

}

case class Column(
                   index: Int,
                   name: String,
                   `type`: String
                 ) {

}
