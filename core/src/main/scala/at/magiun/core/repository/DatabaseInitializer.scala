package at.magiun.core.repository

import scala.concurrent.Await
import scala.concurrent.duration._

class DatabaseInitializer(dataSetRepository: DataSetRepository,
                         blockRepository: BlockRepository) {

  private val sampleCsvUrl = "file://" + getClass.getClassLoader.getResource("insurance_sample.csv").getFile

  def init(): Unit = {
    insertDataSet(MagiunDataSetEntity(1, "people", "FileCsv", sampleCsvUrl))
    insertDataSet(MagiunDataSetEntity(2, "drinks", "Mongo", "mongodb://127.0.0.1/testDb/testCollection"))

    insertBlock(BlockEntity("id-2", "FileReader", s"""{"inputs":[],"params":{"fileName":"$sampleCsvUrl"}}"""))
    insertBlock(BlockEntity("id-3", "LinearRegression", """{"inputs":[["id-2", 0]],"params":{"maxIter":"3"}}"""))
    insertBlock(BlockEntity("id-4", "DropColumn", """{"inputs":[["id-2", 0]],"params":{"columnName":"statecode"}}"""))
    insertBlock(BlockEntity("id-5", "FileWriter", s"""{"inputs":[["id-4", 0]],"params":{"fileName":"$sampleCsvUrl-tmp"}}"""))
  }

  private def insertDataSet(magiunDataSetEntity: MagiunDataSetEntity): Unit = {
    Await.result(dataSetRepository.upsert(magiunDataSetEntity), 5.seconds)
  }

  private def insertBlock(blockEntity: BlockEntity) = {
    Await.result(blockRepository.upsert(blockEntity), 5.seconds)
  }


}
