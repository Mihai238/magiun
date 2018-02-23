package at.magiun.core

import at.magiun.core.model.{MagiunDataSet, Schema, SourceType}

object TestUtils {

  val testDs1 = MagiunDataSet(1, "gigi", SourceType.Mongo, "url", Schema(List.empty))

}
