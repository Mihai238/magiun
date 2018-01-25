package at.magiun.core.repository

import at.magiun.core.MainModule
import at.magiun.core.model.{MagiunDataSet, SourceType}
import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.duration._
import scala.concurrent.Await

class DataSetRepositoryTest extends FlatSpec with Matchers {

  private val TIMEOUT = 1.second

  private val mainModule = new MainModule {}
  private val repo = mainModule.dataSetRepository

  private val dataSet1 = MagiunDataSet(32, "people", SourceType.FileCsv, "file://home/")

  it should "insert a data set" in {
    Await.result(repo.upsert(dataSet1), TIMEOUT)
    val result = Await.result(repo.find(dataSet1.id), TIMEOUT).get

    result.id should equal(dataSet1.id)
    result.name should equal("people")
    result.sourceType should equal(SourceType.FileCsv)
    result.url should equal("file://home/")
  }

  it should "update data set" in {
    Await.result(repo.upsert(dataSet1), TIMEOUT)
    Await.result(repo.upsert(dataSet1.copy(name = "someOtherName", sourceType = SourceType.Mongo)), TIMEOUT)
    val result = Await.result(repo.find(dataSet1.id), TIMEOUT).get

    result.id should equal(dataSet1.id)
    result.name should equal("someOtherName")
    result.sourceType should equal(SourceType.Mongo)
    result.url should equal("file://home/")
  }

  it should "delete a data set" in {
    Await.result(repo.upsert(dataSet1), TIMEOUT)
    Await.result(repo.delete(dataSet1.id), TIMEOUT)

    val result = Await.result(repo.find(dataSet1.id), TIMEOUT)
    result shouldBe None
  }

}
