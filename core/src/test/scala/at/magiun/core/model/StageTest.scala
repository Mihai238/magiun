package at.magiun.core.model

import at.magiun.core.MainModule
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{FlatSpec, Matchers}

@RunWith(classOf[JUnitRunner])
class StageTest extends FlatSpec with Matchers {

  private val mainModule = new MainModule {}
  private val sampleCsvPath = getClass.getClassLoader.getResource("insurance_sample.csv").getFile

  it should "remove the column called 'statecode'" in {
    val task = new DropColumnDecorator(
      new ReaderStage(mainModule.spark, sampleCsvPath),
      "statecode"
    )

    task.perform.head match {
      case DatasetOutput(dataSet) =>
        dataSet.columns.length should be(17)
    }
  }

}
