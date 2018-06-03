package at.magiun.core.service

import at.magiun.core.TestData._
import at.magiun.core.model.Execution
import at.magiun.core.repository.DataSetRepository
import at.magiun.core.{MainModule, UnitTest}

import scala.concurrent.{Await, Future}

class DataSetServiceTest extends UnitTest {

  private val mainModule = new MainModule {
    override lazy val dataSetRepository: DataSetRepository = stub[DataSetRepository]
    override lazy val blockService: BlockService = stub[BlockService]
  }

  private val service = mainModule.dataSetService
  private val executionService: ExecutionService = mainModule.executionService
  private val mockedRepo = mainModule.dataSetRepository
  private val mockedBlockSerive = mainModule.blockService

  it should "return a data set model" in {
    mockedRepo.find _ when 1 returns Future.successful(Option(testDsEntity1))

    val ds = Await.result(service.find(1), TIMEOUT).get
    ds.name should be ("gigi")
    ds.dataSetSource.url should be (testDsEntity1.url)
    ds.schema.get.columns should have size 12
  }

  it should "return rows from a memory ds" in {
    mockedBlockSerive.find _ when "id-3" returns Future.successful(fileReaderBlock)
    mockedBlockSerive.find _ when "id-2" returns Future.successful(dropColBlock)

    val execution = Await.result(executionService.execute(Execution(blockId = "id-2")), TIMEOUT)

    val rows = Await.result(service.findRows(execution.id), TIMEOUT).get
    rows should have size 891
  }

}
