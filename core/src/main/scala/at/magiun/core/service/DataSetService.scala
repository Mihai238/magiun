package at.magiun.core.service

import at.magiun.core.connector.Connector
import at.magiun.core.feature.{Recommendations, Recommender}
import at.magiun.core.model.data.Distribution
import at.magiun.core.model.{DataRow, DataSetSource, MagiunDataSet, SourceType}
import at.magiun.core.repository.{DataSetRepository, MagiunDataSetEntity}
import at.magiun.core.statistics.StatisticsCalculator
import com.typesafe.scalalogging.LazyLogging
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import scala.Option._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DataSetService(
                      spark: SparkSession,
                      dataSetRepository: DataSetRepository,
                      executionContext: ExecutionContext,
                      recommender: Recommender,
                      statisticsCalculator: StatisticsCalculator
                    ) extends LazyLogging {

  def find(id: String): Future[Option[MagiunDataSet]] = {
    if (isMemoryDataSet(id)) {
      Future {
        Option {
          val source = DataSetSource(SourceType.Memory, id)
          val schema = getConnector(SourceType.Memory).getSchema(source)
          MagiunDataSet(id, id, source, Option(schema))
        }
      }
    } else {
      dataSetRepository.find(id.toLong)
        .map(_.map(mapToModel))
    }
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
      entity.id.toString,
      entity.name,
      source,
      Option(schema)
    )
  }

  private def mapToEntity(dataSet: MagiunDataSet): MagiunDataSetEntity = {
    MagiunDataSetEntity(
      dataSet.id.toLong,
      dataSet.name,
      dataSet.dataSetSource.sourceType.toString,
      dataSet.dataSetSource.url
    )
  }

  def findRows(dataSetId: String, range: Option[Range] = empty, columns: Option[Seq[String]] = empty): Future[Option[Seq[DataRow]]] = {
    getConnectorAndSource(dataSetId)
      .map(_.map { case (connector, source) =>
        connector.getRows(source, range, columns)
      })
  }

  def getRandomSample(dataSetId: String, size: Option[Int] = empty, columns: Option[Seq[String]] = empty): Future[Option[Seq[DataRow]]] = {
    getConnectorAndSource(dataSetId)
      .map(_.map { case (connector, source) =>
        connector.getRandomSample(source, size, columns)
      })
  }

  def getDistributions(dataSetId: String): Future[Option[Map[String, Distribution]]] = {
    getConnectorAndSource(dataSetId)
      .map(_.map { case (connector, source) =>
        statisticsCalculator.calculateDistributions(connector.getRandomSampleDF(source))
      })

  }

  def getRecommendations(dataSetId: String): Future[Option[Recommendations]] = {
    getDataSet(dataSetId)
      .map(_.map(ds => recommender.recommend(ds)))
  }

  def getDataSet(dataSetId: String): Future[Option[Dataset[Row]]] = {
    getConnectorAndSource(dataSetId)
      .map(_.map { case (connector, source) =>
        connector.getDataset(source)
      })
  }

  private def getConnectorAndSource(dataSetId: String): Future[Option[(Connector, DataSetSource)]] = {
    if (isMemoryDataSet(dataSetId)) {
      val source = DataSetSource(SourceType.Memory, dataSetId)
      val connector = getConnector(SourceType.Memory)
      Future {
        Option {
          (connector, source)
        }
      }
    } else {
      find(dataSetId)
        .map(_.map(ds => {
          val connector = getConnector(ds.dataSetSource.sourceType)
          (connector, ds.dataSetSource)
        }))
    }
  }

  private def getConnector(sourceType: SourceType): Connector = {
    logger.info("Getting connector for " + sourceType)
    new ConnectorFactory(spark, executionContext).getConnector(sourceType)
  }

  private def isMemoryDataSet(id: String) = {
    id.startsWith("mem-")
  }

}
