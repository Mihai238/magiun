package at.magiun.core.rest

import at.magiun.core.TestData._
import at.magiun.core.model.MagiunDataSet
import at.magiun.core.service.DataSetService
import at.magiun.core.{MainModule, UnitTest}
import com.twitter.io.Buf
import io.finch.{Application, Input}
import org.scalamock.function.FunctionAdapter1

import scala.concurrent.Future

class DataSetControllerTest extends UnitTest {

  private val mainModule = new MainModule {
    override lazy val dataSetService: DataSetService = stub[DataSetService]
  }

  val stubService: DataSetService = mainModule.dataSetService
  val controller: DataSetController = mainModule.dataSetController

  it should "return a dataset" in {
    val input = Input.get("/datasets/1")
    stubService.find _ when 1 returns Future.successful(Option(testDs1))

    val result = controller.getDataSet(input)

    val dataSet = result.awaitValueUnsafe().get
    dataSet.id should be(1)
    dataSet.name should be("gigi")
  }

  it should "return created dataset" in {
    val input = Input.post("/datasets/").withBody[Application.Json](Buf.Utf8("""{"id": "0", "name":"foo", "dataSetSource":{"sourceType":"FileCsv","url":"file:///home/mihai/Development/thesis/magiun/core/target/classes/insurance_sample.csv"}}"""))
    stubService.create _ when * returns Future.successful(testDs1)

    val result = controller.createDataSet(input)

    val dataSet = result.awaitValueUnsafe().get
    dataSet.id should be (1)

    val matcher = where  {
      (ds: MagiunDataSet) => ds.id == 0 && ds.name == "foo"
    }
    stubService.create _ verify matcher
  }

}