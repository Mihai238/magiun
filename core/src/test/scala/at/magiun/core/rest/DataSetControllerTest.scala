package at.magiun.core.rest

import at.magiun.core.model.{MagiunDataSet, Schema, SourceType}
import at.magiun.core.service.DataSetService
import io.finch.Input
import org.junit.runner.RunWith
import org.scalamock.scalatest.MockFactory
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class DataSetControllerTest extends FlatSpec with Matchers with MockFactory {

  val ds1 = MagiunDataSet(1, "gigi", SourceType.Mongo, "url", Schema(List.empty))

  it should "return a dataset" in {
    val input = Input.get("/datasets/1")

    val mockedService = stub[DataSetService]
    mockedService.find _ when 1 returns Future.successful(Option(ds1))

    val result = new DataSetController(mockedService).getDataSet(input)

    val dataSet = result.awaitValueUnsafe().get
    dataSet.id should be (1)
    dataSet.name should be ("gigi")
  }

}
