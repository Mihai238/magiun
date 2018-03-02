package at.magiun.core.model

import at.magiun.core.TestData.sampleCsvPath
import at.magiun.core.executor.{DropColumnStage, FileReaderStage, AddColumnStage, StageInput}
import at.magiun.core.{MainModule, UnitTest}
import org.apache.spark.sql.functions.udf

class StageTest extends UnitTest {

  private val mainModule = new MainModule {}

  private val input = StageInput(new FileReaderStage(mainModule.spark, sampleCsvPath))

  it should "remove the column called 'statecode'" in {
    val task = new DropColumnStage(
      input,
      "statecode"
    )

    task.perform match {
      case DatasetOutput(dataSet) =>
        dataSet.columns.length should be(17)
    }
  }

  it should "add a new column" in {
    val task = new AddColumnStage(
      input,
      "hihiCol",
      "eq_site_limit + tiv_2012"
    )

    task.perform match {
      case DatasetOutput(dataSet) =>
        dataSet.columns.length should be(19)
        dataSet.take(1).head.getAs[Double]("hihiCol") should be(1291108.9)
    }
  }

  it should "remove and add a column" in {
    val task = new AddColumnStage(
      StageInput(new DropColumnStage(input, "policyID")),
      "new_col_1",
      "eq_site_limit + tiv_2012"
    )

    task.perform match {
      case DatasetOutput(dataSet) =>
        dataSet.columns.length should be(18)
        dataSet.columns should not contain "policyID"
        dataSet.take(1).head.getAs[Double]("new_col_1") should be(1291108.9)
    }
  }


}
