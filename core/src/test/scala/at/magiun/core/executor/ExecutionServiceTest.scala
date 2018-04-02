package at.magiun.core.executor

import at.magiun.core.TestData.sampleCsvPath
import at.magiun.core.model.{Block, BlockType, DatasetOutput, BlockInput}
import at.magiun.core.{MainModule, UnitTest}

class ExecutionServiceTest extends UnitTest {

  private val mainModule = new MainModule {}
  private val executor = mainModule.executor

  it should "execute a sequence of stages" in {
    val fileReaderBlock = Block("id-3", BlockType.FileReader, Seq.empty, Map("fileName" -> sampleCsvPath))
    val dropColBlock = Block("id-2", BlockType.DropColumn, Seq(BlockInput("id-3", 0)), Map("columnName" -> "statecode"))
    val blocks = Map("id-3" ->  fileReaderBlock, "id-2" -> dropColBlock)

    val output = executor.execute(blocks, dropColBlock)

    output match {
      case DatasetOutput(ds) =>
        ds.columns.length should be (17)
    }
  }

}
