package at.magiun.core.repository

import at.magiun.core.model.SourceType

import scala.concurrent.Await
import scala.concurrent.duration._

class DatabaseInitializer(dataSetRepository: DataSetRepository) {

  def init(): Unit = {
    Await.result(dataSetRepository.upsert(MagiunDataSetEntity(1, "people", SourceType.FileCsv, "/home/mihai/f.txt")), 5.seconds)
    Await.result(dataSetRepository.upsert(MagiunDataSetEntity(2, "drinks", SourceType.Mongo, "mongodb://db1.example.net:27017")), 5.seconds)
  }


}
