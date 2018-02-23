package at.magiun.core.rest

import at.magiun.core.model.MagiunDataSet
import at.magiun.core.service.DataSetService
import com.twitter.util.{Return, Throw, Future => TFuture, Promise => TPromise}
import io.finch._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future => SFuture, Promise => SPromise}
import scala.util.{Failure, Success}

class DataSetController(dataSetService: DataSetService) {

  private val PATH = "datasets"

  //noinspection TypeAnnotation
  lazy val api = getDataSet :+: getDataSets

  val getDataSet: Endpoint[MagiunDataSet] = get(PATH :: path[Int]) { id: Int =>

    dataSetService.find(id).asTwitter.map(e => e.get).map(Ok)
  }

  val getDataSets: Endpoint[Seq[MagiunDataSet]] = get(PATH) {
    dataSetService.findAll()
      .asTwitter
      .map(Ok)
  }


  // Future conversions

  implicit class RichTFuture[A](f: TFuture[A]) {
    def asScala(implicit e: ExecutionContext): SFuture[A] = {
      val p: SPromise[A] = SPromise()
      f.respond {
        case Return(value) => p.success(value)
        case Throw(exception) => p.failure(exception)
      }

      p.future
    }
  }

  implicit class RichSFuture[A](f: SFuture[A]) {
    def asTwitter(implicit e: ExecutionContext): TFuture[A] = {
      val p: TPromise[A] = new TPromise[A]
      f.onComplete {
        case Success(value) => p.setValue(value)
        case Failure(exception) => p.setException(exception)
      }

      p
    }
  }

}