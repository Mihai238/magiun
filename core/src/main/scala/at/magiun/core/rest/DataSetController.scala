package at.magiun.core.rest

import at.magiun.core.feature.Recommendations
import at.magiun.core.model.{DataRow, MagiunDataSet}
import at.magiun.core.rest.FutureConverter._
import at.magiun.core.service.DataSetService
import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._

import scala.concurrent.ExecutionContext.Implicits.global

class DataSetController(dataSetService: DataSetService) extends LazyLogging {

  private val BASE_PATH = "datasets"
  private val ROWS_PATH = "rows"
  private val SAMPLE_PATH = "sample"
  private val RECOMMENDATIONS_PATH = "recommendations"

  //noinspection TypeAnnotation
  lazy val api = getDataSet :+: getDataSets :+: createDataSet :+: getRows :+: getRandomSample :+: getRecommendation

  val getDataSet: Endpoint[MagiunDataSet] = get(BASE_PATH :: path[String]) { id: String =>

    dataSetService.find(id)
      .asTwitter
      .map(e => e.get)
      .map(Ok)
  }

  val getDataSets: Endpoint[Seq[MagiunDataSet]] = get(BASE_PATH) {
    logger.info("Getting all datasets")

    dataSetService.findAll()
      .asTwitter
      .map(Ok)
  }

  val createDataSet: Endpoint[MagiunDataSet] = post(BASE_PATH :: jsonBody[MagiunDataSet]) { dataSet: MagiunDataSet =>
    logger.info("Creating new dataset")

    dataSetService.create(dataSet)
      .asTwitter
      .map(Ok)
  }

  /**
    * returns the firsts rows
    * range based on ${limit} and ${page}
    */
  val getRows: Endpoint[Seq[DataRow]] = get(BASE_PATH :: path[String] :: ROWS_PATH ::
    paramOption("_limit") :: paramOption("_page") :: paramOption("_columns")) {

    (dataSetId: String, limit: Option[String], page: Option[String], stringColumns: Option[String]) =>
      logger.info(s"Getting rows for dataset `$dataSetId` with limit `$limit` and page `$page` and cols `$stringColumns`")

      val range = for {
        l <- limit
        p <- page.orElse(Option("1"))
        limit = Integer.parseInt(l)
        page = Integer.parseInt(p)
      } yield Range((page - 1) * limit, page * limit + 1)

      val columns = splitString(stringColumns)

      dataSetService.findRows(dataSetId, range, columns)
        .asTwitter
        .map(_.get)
        .map(Ok)
  }

  val getRandomSample: Endpoint[Seq[DataRow]] = get(BASE_PATH :: path[String] :: SAMPLE_PATH ::
  paramOption("_size") :: paramOption("_columns")) {

    (datasetId: String, size: Option[String], columns: Option[String]) =>
      logger.info(s"Getting a random sample of the size `$size` for the columns `$columns` of the dataset `$datasetId`")

      dataSetService.getRandomSample(datasetId, size.map(s => s.toInt), splitString(columns))
        .asTwitter
        .map(_.get)
        .map(Ok)

  }

  private def splitString(value: Option[String], separator: String = ","): Option[Seq[String]] = {
    value.map(_.split(separator).map(_.trim).toSeq)
  }

  val getRecommendation: Endpoint[Recommendations] = get(BASE_PATH :: path[String] :: RECOMMENDATIONS_PATH) {
    dataSetId: String =>
      logger.info(s"Getting recommendations for dataset `$dataSetId`")

      dataSetService.getRecommendations(dataSetId)
        .asTwitter
        .map(e => e.get)
        .map(Ok)
  }


}