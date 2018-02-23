package at.magiun.core.rest

import at.magiun.core.TestUtils._
import at.magiun.core.UnitTest
import at.magiun.core.service.DataSetService
import io.finch.Input

import scala.concurrent.Future

class DataSetControllerTest extends UnitTest {

  it should "return a dataset" in {
    val input = Input.get("/datasets/1")

    val mockedService = stub[DataSetService]
    mockedService.find _ when 1 returns Future.successful(Option(testDs1))

    val result = new DataSetController(mockedService).getDataSet(input)

    val dataSet = result.awaitValueUnsafe().get
    dataSet.id should be(1)
    dataSet.name should be("gigi")
  }

}
