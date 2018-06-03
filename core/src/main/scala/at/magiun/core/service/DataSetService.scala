package at.magiun.core.service

import at.magiun.core.connector.{Connector, CsvConnector, MemoryConnector, MongoDbConnector}
import at.magiun.core.model.SourceType.{FileCsv, Memory, Mongo}
import at.magiun.core.model.{DataRow, DataSetSource, MagiunDataSet, SourceType}
import at.magiun.core.repository.{DataSetRepository, MagiunDataSetEntity}
import org.apache.spark.sql.SparkSession

import scala.Option._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DataSetService(
                      dataSetRepository: DataSetRepository,
                      executionService: ExecutionService,
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

  def create(dataSet: MagiunDataSet): Future[MagiunDataSet] = {
    dataSetRepository.upsert(mapToEntity(dataSet))
      .map(mapToModel)
  }

  private def mapToModel(entity: MagiunDataSetEntity): MagiunDataSet = {
    val sourceType = SourceType.withName(entity.sourceType)
    val source = DataSetSource(sourceType, entity.url)
    val schema = getConnector(sourceType).getSchema(source)

    MagiunDataSet(
      entity.id,
      entity.name,
      source,
      Option(schema)
    )
  }

  private def mapToEntity(dataSet: MagiunDataSet): MagiunDataSetEntity = {
    MagiunDataSetEntity(
      dataSet.id,
      dataSet.name,
      dataSet.dataSetSource.sourceType.toString,
      dataSet.dataSetSource.url
    )
  }

  def findRows(dataSetId: String, range: Option[Range] = empty, columns: Option[Set[String]] = empty): Future[Option[Seq[DataRow]]] = {
    if (dataSetId.contains("-")) {
      val source = DataSetSource(SourceType.Memory, dataSetId)
      val connector = getConnector(SourceType.Memory)
      Future {
        Option {
          connector.getRows(source, range, columns)
        }
      }
    } else {
      find(dataSetId.toLong)
        .map(_.map(ds => {
          val connector = getConnector(ds.dataSetSource.sourceType)
          connector.getRows(ds.dataSetSource, range, columns)
        }))
    }
  }

  private def getConnector(sourceType: SourceType): Connector = sourceType match {
    case FileCsv => new CsvConnector(sparkSession)
    case Mongo => new MongoDbConnector(sparkSession)
    case Memory => new MemoryConnector(executionService.getExecutionsOutput)
  }

}
