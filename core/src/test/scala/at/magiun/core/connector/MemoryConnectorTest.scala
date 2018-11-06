package at.magiun.core.connector

import at.magiun.core.TestData._
import at.magiun.core.model.{DataSetSource, SourceType}
import at.magiun.core.service.ExecutionService
import at.magiun.core.{MainModule, UnitTest}

class MemoryConnectorTest extends UnitTest {

  private val mainModule = new MainModule {}

  private val executionService: ExecutionService = mainModule.executionService

  it should "get schema given a memory dataset" in {
    val blocks = Map("id-3" -> fileReaderBlock, "id-2" -> dropColBlock)

    val output = executionService.execute(blocks, dropColBlock)

    val memoryConnector = new MemoryConnector(Map("1-1" -> output))
    val rows = memoryConnector.getRows(DataSetSource(SourceType.Memory, "1-1"))

    rows should have size 891
  }

}
