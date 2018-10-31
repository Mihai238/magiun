package at.magiun.core.repository

import scala.concurrent.Await
import scala.concurrent.duration._

class DatabaseInitializer(dataSetRepository: DataSetRepository,
                         blockRepository: BlockRepository) {

  private val sampleCsvUrl = "file://" + getClass.getClassLoader.getResource("titanic.csv").getFile
  private val incomeCsvUrl = "file://" + getClass.getClassLoader.getResource("income.csv").getFile
  private val bigCsvUrl = "file://" + getClass.getClassLoader.getResource("big.csv").getFile

  def init(): Unit = {
    insertDataSet(MagiunDataSetEntity(0, "titanic", "FileCsv", sampleCsvUrl))
    insertDataSet(MagiunDataSetEntity(0, "income", "FileCsv", incomeCsvUrl))
//    insertDataSet(MagiunDataSetEntity(0, "big", "FileCsv", bigCsvUrl))
//    insertDataSet(MagiunDataSetEntity(2, "drinks", "Mongo", "mongodb://127.0.0.1/testDb/testCollection"))

    insertBlock(BlockEntity("id-2", "FileReader", s"""{"inputs":[],"params":{"fileName":"$sampleCsvUrl"}}"""))
    insertBlock(BlockEntity("id-3", "LinearRegression", """{"inputs":[{"blockId": "id-2", "index": 0}],"params":{"maxIter":"3"}}"""))
    insertBlock(BlockEntity("id-4", "DropColumn", """{"inputs":[{"blockId": "id-2", "index": 0}],"params":{"columnName":"PassengerId"}}"""))
    insertBlock(BlockEntity("id-5", "FileWriter", s"""{"inputs":[{"blockId": "id-4", "index": 0}],"params":{"fileName":"$sampleCsvUrl-tmp"}}"""))
  }

  private def insertDataSet(magiunDataSetEntity: MagiunDataSetEntity): Unit = {
    Await.result(dataSetRepository.upsert(magiunDataSetEntity), 5.seconds)
  }

  private def insertBlock(blockEntity: BlockEntity) = {
    Await.result(blockRepository.upsert(blockEntity), 5.seconds)
  }


}
