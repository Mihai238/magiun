package at.magiun.core.service

import at.magiun.core.connector.{Connector, CsvConnector, MongoDbConnector}
import at.magiun.core.model.SourceType.{FileCsv, Mongo}
import at.magiun.core.model.{DataSetSource, MagiunDataSet, SourceType}
import at.magiun.core.repository.{DataSetRepository, MagiunDataSetEntity}
import org.apache.spark.sql.SparkSession

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DataSetService(
                      dataSetRepository: DataSetRepository,
                      sparkSession: SparkSession
                    ) {

  def find(id: Long): Future[Option[MagiunDataSet]] = {
    dataSetRepository.find(id)
      .map(_.map(mapToModel))
  }

  def findAll(): Future[Seq[MagiunDataSet]] = {
    dataSetRepository.findAll()
      .map(_.map(mapToModel))
  }

  private def mapToModel(entity: MagiunDataSetEntity): MagiunDataSet = {
    val source = DataSetSource(entity.sourceType, entity.url)
    val schema = getConnector(entity.sourceType).getSchema(source)

    MagiunDataSet(
      entity.id,
      entity.name,
      source,
      schema
    )
  }

  private def getConnector(sourceType: SourceType): Connector = sourceType match {
    case FileCsv => new CsvConnector(sparkSession)
    case Mongo => new MongoDbConnector
  }

}
