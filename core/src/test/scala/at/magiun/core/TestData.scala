package at.magiun.core

import at.magiun.core.model._
import at.magiun.core.repository.{BlockEntity, MagiunDataSetEntity}

object TestData {

  val titanicCsvPath: String = getClass.getClassLoader.getResource("titanic.csv").getFile
  val titanicCsvUrl = s"file://$titanicCsvPath"

  val incomeCsvPath: String = getClass.getClassLoader.getResource("income.csv").getFile
  val incomeCsvUrl = s"file://$incomeCsvPath"

  // https://www.kaggle.com/c/acm-sf-chapter-hackathon-big/data
  lazy val bigCsvPath: String = getClass.getClassLoader.getResource("big.csv").getFile
  val bigCsvUrl = s"file://$bigCsvPath"

  val titanicDataSetSource = DataSetSource(SourceType.FileCsv, titanicCsvUrl)
  val incomeDataSetSource = DataSetSource(SourceType.FileCsv, incomeCsvUrl)
  val bigDataSetSource = DataSetSource(SourceType.FileCsv, bigCsvUrl)
  val mongoDataSource = DataSetSource(SourceType.Mongo, "mongodb://127.0.0.1/testDb/testCollection")

  val testDs1 = MagiunDataSet("1", "gigi", DataSetSource(SourceType.Mongo, "url") , Option.empty)
  val testDsEntity1 = MagiunDataSetEntity(1, "gigi", "FileCsv", titanicCsvUrl)

  val testBlock2 = Block("id-2", BlockType.FileReader, Seq(BlockInput("1", 0)), params = Map("x" -> "4"))
  val testBlockEntity1 = BlockEntity("id-2", "FileReader", """{"inputs":[{"blockId":"1","index":0}],"params":{"x":"4"}}""")

  val fileReaderBlock = Block("id-3", BlockType.FileReader, Seq.empty, Map("fileName" -> titanicCsvPath))
  val dropColBlock = Block("id-2", BlockType.DropColumn, Seq(BlockInput("id-3", 0)), Map("columnName" -> "Parch"))

}
