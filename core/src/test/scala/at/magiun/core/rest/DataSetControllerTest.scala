package at.magiun.core.rest

import at.magiun.core.TestUtils._
import at.magiun.core.service.DataSetService
import at.magiun.core.{MainModule, UnitTest}
import io.finch.Input

import scala.concurrent.Future

class DataSetControllerTest extends UnitTest {

  private val mainModule = new MainModule {
    override lazy val dataSetService: DataSetService = stub[DataSetService]
  }

  it should "return a dataset" in {
    val input = Input.get("/datasets/1")
    mainModule.dataSetService.find _ when 1 returns Future.successful(Option(testDs1))

    val result = mainModule.dataSetController.getDataSet(input)

    val dataSet = result.awaitValueUnsafe().get
    dataSet.id should be(1)
    dataSet.name should be("gigi")
  }

}
