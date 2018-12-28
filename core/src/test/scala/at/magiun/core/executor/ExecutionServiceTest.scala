package at.magiun.core.executor

import at.magiun.core.TestData._
import at.magiun.core.model._
import at.magiun.core.service.BlockService
import at.magiun.core.{MainModule, UnitTest}

import scala.concurrent.{Await, Future}

class ExecutionServiceTest extends UnitTest {

  private val mainModule = new MainModule {
    override lazy val blockService: BlockService = stub[BlockService]
  }

  private val executionService = mainModule.executionService
  private val mockedBlockSerive = mainModule.blockService

  it should "execute a sequence of stages" in {
    val blocks = Map("id-3" -> fileReaderBlock, "id-2" -> dropColBlock)

    val output = executionService.execute(blocks, dropColBlock)

    output match {
      case DatasetOutput(ds) =>
        ds.columns.length should be(11)
    }
  }

  it should "build and execute a sequence of stages" in {
    mockedBlockSerive.find _ when "id-3" returns Future.successful(fileReaderBlock)
    mockedBlockSerive.find _ when "id-2" returns Future.successful(dropColBlock)

    val execution = Await.result(executionService.execute(Execution(blockId = "id-2")), TIMEOUT)
    execution.id should be ("mem-1")

    val output = executionService.executionContext.getExecutionOutput(execution.id)
    output match {
      case DatasetOutput(ds) =>
        ds.columns.length should be(11)
    }
  }

}
