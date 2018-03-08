package at.magiun.core.rest

import at.magiun.core.model.{MagiunDataSet, DataRow}
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

  //noinspection TypeAnnotation
  lazy val api = getDataSet :+: getDataSets :+: createDataSet :+: getRows

  val getDataSet: Endpoint[MagiunDataSet] = get(BASE_PATH :: path[Int]) { id: Int =>

    dataSetService.find(id)
      .asTwitter
      .map(e => e.get)
      .map(Ok)
  }

  val getDataSets: Endpoint[Seq[MagiunDataSet]] = get(BASE_PATH) {
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

  val getRows: Endpoint[Seq[DataRow]] = get(BASE_PATH :: path[Int] :: ROWS_PATH ::
    paramOption("_limit") :: paramOption("_page") :: paramOption("_columns")) {

    (dataSetId: Int, limit: Option[String], page: Option[String], stringColumns: Option[String]) =>
      logger.info(s"Getting rows for dataset `$dataSetId` with limit `$limit` and page `$page` and cols `$stringColumns`")

      val range = for {
        l <- limit
        p <- page
        limit = Integer.parseInt(l)
        page = Integer.parseInt(p)
      } yield Range((page - 1) * limit, page * limit + 1)

      val columns = stringColumns.map(_.split(",").map(_.trim).toSet)

      dataSetService.findRows(dataSetId, range, columns)
        .asTwitter
        .map(_.get)
        .map(Ok)
  }


}