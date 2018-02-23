package at.magiun.core

import at.magiun.core.model.{DataSetSource, MagiunDataSet, Schema, SourceType}
import at.magiun.core.repository.MagiunDataSetEntity

object TestData {

  val sampleCsvPath: String = getClass.getClassLoader.getResource("insurance_sample.csv").getFile
  val sampleCsvUrl = s"file://$sampleCsvPath"

  val testDsEntity1 = MagiunDataSetEntity(1, "gigi", SourceType.FileCsv, sampleCsvUrl)

  val testDs1 = MagiunDataSet(1, "gigi", DataSetSource(SourceType.Mongo, "url") , Schema(List.empty))
  val csvDataSetSource = DataSetSource(SourceType.FileCsv, sampleCsvUrl)
  val mongoDataSource = DataSetSource(SourceType.Mongo, "mongodb://127.0.0.1/testDb/testCollection")

}
