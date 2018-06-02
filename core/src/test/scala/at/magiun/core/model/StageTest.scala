package at.magiun.core.model

import at.magiun.core.TestData.sampleCsvPath
import at.magiun.core.{MainModule, UnitTest}

class StageTest extends UnitTest {

  private val mainModule = new MainModule {}

  private val input = StageInput(new FileReaderStage(mainModule.spark, sampleCsvPath))

  it should "remove the column called 'statecode'" in {
    val task = new DropColumnStage(
      input,
      "Cabin"
    )

    task.perform match {
      case DatasetOutput(dataSet) =>
        dataSet.columns.length should be(11)
    }
  }

  it should "add a new column" in {
    val task = new AddColumnStage(
      input,
      "hihiCol",
      "Pclass + Age"
    )

    task.perform match {
      case DatasetOutput(dataSet) =>
        dataSet.columns.length should be(13)
        dataSet.take(1).head.getAs[Double]("hihiCol") should be(25)
    }
  }

  it should "remove and add a column" in {
    val task = new AddColumnStage(
      StageInput(new DropColumnStage(input, "Cabin")),
      "new_col_1",
      "Pclass + Age"
    )

    task.perform match {
      case DatasetOutput(dataSet) =>
        dataSet.columns.length should be(12)
        dataSet.columns should not contain "Cabin"
        dataSet.take(1).head.getAs[Double]("new_col_1") should be(25)
    }
  }


}
