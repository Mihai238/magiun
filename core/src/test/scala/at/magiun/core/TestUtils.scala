package at.magiun.core

import at.magiun.core.model.{DataSetSource, MagiunDataSet, Schema, SourceType}

object TestUtils {

  val sampleCsvPath: String = getClass.getClassLoader.getResource("insurance_sample.csv").getFile

  val testDs1 = MagiunDataSet(1, "gigi", DataSetSource(SourceType.Mongo, "url") , Schema(List.empty))
  val csvDataSetSource = DataSetSource(SourceType.FileCsv, s"file://$sampleCsvPath")

}
