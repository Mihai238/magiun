package at.magiun.core.model

import at.magiun.core.TestData.sampleCsvPath
import at.magiun.core.{MainModule, UnitTest}

class StageTest extends UnitTest {

  private val mainModule = new MainModule {}

  it should "remove the column called 'statecode'" in {
    val task = new DropColumnStage(
      new ReaderStage(mainModule.spark, sampleCsvPath),
      "statecode"
    )

    task.perform match {
      case DatasetOutput(dataSet) =>
        dataSet.columns.length should be(17)
    }
  }

}
