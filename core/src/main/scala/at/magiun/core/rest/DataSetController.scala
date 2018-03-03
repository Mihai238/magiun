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
  private val ROWS_PATH = "/rows"

  //noinspection TypeAnnotation
  lazy val api = getDataSet :+: getDataSets :+: createDataSet

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

  val getRows: Endpoint[Seq[DataRow]] = get(BASE_PATH + ROWS_PATH :: path[Int]) { dataSetId: Int =>
    dataSetService.findRows(dataSetId)
      .asTwitter
      .map(_.get)
      .map(Ok)
  }


}