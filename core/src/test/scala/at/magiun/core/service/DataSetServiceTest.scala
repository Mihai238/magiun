package at.magiun.core.service

import at.magiun.core.TestData.{sampleCsvUrl, testDsEntity1}
import at.magiun.core.repository.DataSetRepository
import at.magiun.core.{MainModule, TestData, UnitTest}

import scala.concurrent.{Await, Future}

class DataSetServiceTest extends UnitTest {

  private val mainModule = new MainModule {
    override lazy val dataSetRepository: DataSetRepository = stub[DataSetRepository]
  }

  val service: DataSetService = mainModule.dataSetService
  val mockedRepo: DataSetRepository = mainModule.dataSetRepository

  it should "return a data set model" in {
    mockedRepo.find _ when 1 returns Future.successful(Option(testDsEntity1))

    val ds = Await.result(service.find(1), TIMEOUT).get
    ds.name should be ("gigi")
    ds.dataSetSource.url should be (testDsEntity1.url)
    ds.schema.columns should have size 18
  }

}
