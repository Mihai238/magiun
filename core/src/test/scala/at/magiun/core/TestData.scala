package at.magiun.core

import at.magiun.core.model._
import at.magiun.core.repository.{BlockEntity, MagiunDataSetEntity}

object TestData {

  val sampleCsvPath: String = getClass.getClassLoader.getResource("insurance_sample.csv").getFile
  val sampleCsvUrl = s"file://$sampleCsvPath"

  val csvDataSetSource = DataSetSource(SourceType.FileCsv, sampleCsvUrl)
  val mongoDataSource = DataSetSource(SourceType.Mongo, "mongodb://127.0.0.1/testDb/testCollection")

  val testDs1 = MagiunDataSet(1, "gigi", DataSetSource(SourceType.Mongo, "url") , Option.empty)
  val testDsEntity1 = MagiunDataSetEntity(1, "gigi", "FileCsv", sampleCsvUrl)

  val testBlock2 = Block("id-2", BlockType.FileReader, Seq(BlockInput("1", 0)), params = Map("x" -> "4"))
  val testBlockEntity1 = BlockEntity("id-2", "FileReader", """{"inputs":[{"blockId":"1","index":0}],"params":{"x":"4"}}""")

}
