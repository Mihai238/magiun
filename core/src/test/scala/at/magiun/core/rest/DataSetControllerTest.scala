package at.magiun.core.rest

import at.magiun.core.TestData._
import at.magiun.core.model.MagiunDataSet
import at.magiun.core.service.DataSetService
import at.magiun.core.{MainModule, UnitTest}
import com.twitter.io.Buf
import io.finch.{Application, Input}

import scala.concurrent.ExecutionContext.Implicits.global
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
    val input = Input.post("/datasets/").withBody[Application.Json](Buf.Utf8("""{"id": "0", "name":"foo", "dataSetSource":{"sourceType":"FileCsv","url":"file:///home/mihai/Development/thesis/magiun/core/target/classes/titanic.csv"}}"""))
    stubService.create _ when * returns Future.successful(testDs1)

    val result = controller.createDataSet(input)

    val dataSet = result.awaitValueUnsafe().get
    dataSet.id should be(1)

    val matcher = where {
      ds: MagiunDataSet => ds.id == 0 && ds.name == "foo"
    }
    stubService.create _ verify matcher
  }

  it should "return rows with range" in {
    val input = Input.get("/datasets/1/rows", "_limit" -> "20", "_page" -> "5", "_columns" -> "col1, col2")
    stubService.findRows _ when(*, *, *) returns Future(Option(Seq.empty))

    val result = controller.getRows(input)
    result.awaitValueUnsafe().get

    val matcher = where {
      (id:Int, range: Option[Range], cols: Option[Set[String]]) => range.get.start == 80 && range.get.last == 100 && cols.get.size == 2
    }
    stubService.findRows _ verify matcher
  }

}
