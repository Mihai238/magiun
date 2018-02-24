package at.magiun.core.rest

import at.magiun.core.model.MagiunDataSet
import at.magiun.core.rest.FutureConverter._
import at.magiun.core.service.DataSetService
import com.twitter.util.{Future => TFuture, Promise => TPromise}
import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future => SFuture, Promise => SPromise}

class DataSetController(dataSetService: DataSetService) extends LazyLogging {

  private val PATH = "datasets"

  //noinspection TypeAnnotation
  lazy val api = getDataSet :+: getDataSets :+: createDataSet

  val getDataSet: Endpoint[MagiunDataSet] = get(PATH :: path[Int]) { id: Int =>

    dataSetService.find(id)
      .asTwitter
      .map(e => e.get)
      .map(Ok)
  }

  val getDataSets: Endpoint[Seq[MagiunDataSet]] = get(PATH) {
    dataSetService.findAll()
      .asTwitter
      .map(Ok)
  }

  val createDataSet: Endpoint[MagiunDataSet] = post(PATH :: jsonBody[MagiunDataSet]) { dataSet: MagiunDataSet =>
    logger.info("Creating new dataset")

    dataSetService.create(dataSet)
      .asTwitter
      .map(Ok)
  }


}