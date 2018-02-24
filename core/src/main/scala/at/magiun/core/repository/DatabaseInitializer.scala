package at.magiun.core.repository

import at.magiun.core.model.SourceType

import scala.concurrent.Await
import scala.concurrent.duration._

class DatabaseInitializer(dataSetRepository: DataSetRepository) {

  private val sampleCsvUrl = "file://" + getClass.getClassLoader.getResource("insurance_sample.csv").getFile

  def init(): Unit = {
    insertDataSet(MagiunDataSetEntity(1, "people", "FileCsv", sampleCsvUrl))
    insertDataSet(MagiunDataSetEntity(2, "drinks", "Mongo", "mongodb://127.0.0.1/testDb/testCollection"))
  }

  private def insertDataSet(magiunDataSetEntity: MagiunDataSetEntity): Unit = {
    Await.result(dataSetRepository.upsert(magiunDataSetEntity), 5.seconds)
  }


}
