package at.magiun.core.repository

import at.magiun.core.{MainModule, UnitTest}

import scala.concurrent.Await

class DataSetRepositoryTest extends UnitTest {

  private val mainModule = new MainModule {}
  private val repo = mainModule.dataSetRepository

  private val dataSet1 = MagiunDataSetEntity(1, "people", "FileCsv", "file://home/")
  private val dataSet2 = MagiunDataSetEntity(2, "drinks", "Mongo", "file://drinks/")

  it should "insert a data set" in {
    Await.result(repo.upsert(dataSet1), TIMEOUT)
    val result = Await.result(repo.find(dataSet1.id), TIMEOUT).get

    result.id should equal(dataSet1.id)
    result.name should equal("people")
    result.sourceType should equal("FileCsv")
    result.url should equal("file://home/")
  }

  it should "update data set" in {
    Await.result(repo.upsert(dataSet1), TIMEOUT)
    Await.result(repo.upsert(dataSet1.copy(name = "someOtherName", sourceType = "Mongo")), TIMEOUT)
    val result = Await.result(repo.find(dataSet1.id), TIMEOUT).get

    result.id should equal(dataSet1.id)
    result.name should equal("someOtherName")
    result.sourceType should equal("Mongo")
    result.url should equal("file://home/")
  }

  it should "find all data sets" in {
    Await.result(repo.upsert(dataSet1), TIMEOUT)
    Await.result(repo.upsert(dataSet2), TIMEOUT)

    val dataSets = Await.result(repo.findAll(), TIMEOUT)
    dataSets should have size 2
    dataSets.head.id should be (1)
    dataSets(1).id should be (2)
    dataSets(1).name should be ("drinks")
    dataSets(1).sourceType should be ("Mongo")
  }

  it should "delete a data set" in {
    Await.result(repo.upsert(dataSet1), TIMEOUT)
    Await.result(repo.delete(dataSet1.id), TIMEOUT)

    val result = Await.result(repo.find(dataSet1.id), TIMEOUT)
    result shouldBe None
  }

}
