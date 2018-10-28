package at.magiun.core.feature

import at.magiun.core.TestData.titanicDataSetSource
import at.magiun.core.connector.CsvConnector
import at.magiun.core.{MainModule, UnitTest}

class ColumnMetaDataComputerTest extends UnitTest {

  private val mainModule = new MainModule {}

  private val connector = new CsvConnector(mainModule.spark)
  private val columnMetaDataComputer = mainModule.columnMetaDataComputer
  private val restrictionBuilder = mainModule.restrictionBuilder

  it should "try to guess column types" in {
    val restrictions = restrictionBuilder.build(mainModule.model)
    columnMetaDataComputer.compute(connector.getDataset(titanicDataSetSource), restrictions)
  }

}
