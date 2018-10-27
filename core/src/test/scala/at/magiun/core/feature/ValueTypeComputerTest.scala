package at.magiun.core.feature

import at.magiun.core.TestData.titanicDataSetSource
import at.magiun.core.connector.CsvConnector
import at.magiun.core.{MainModule, UnitTest}

class ValueTypeComputerTest extends UnitTest {

  private val mainModule = new MainModule {}

  private val connector = new CsvConnector(mainModule.spark)
  private val valueTypeComputer = mainModule.valueTypeComputer
  private val restrictionBuilder = mainModule.restrictionBuilder

  it should "try to guess column types" in {
    val restrictions = restrictionBuilder.build(mainModule.model)
    valueTypeComputer.process(connector.getDataset(titanicDataSetSource), restrictions)
  }

}
