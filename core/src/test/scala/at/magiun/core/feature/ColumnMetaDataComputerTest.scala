package at.magiun.core.feature

import at.magiun.core.TestData._
import at.magiun.core.connector.CsvConnector
import at.magiun.core.{MainModule, UnitTest}

class ColumnMetaDataComputerTest extends UnitTest {

  private val mainModule = new MainModule {}

  private val connector = new CsvConnector(mainModule.spark)
  private val columnMetaDataComputer = mainModule.columnMetaDataComputer
  private val restrictionBuilder = mainModule.restrictionBuilder

  private val restrictions = restrictionBuilder.build(mainModule.model)

  it should "try to guess column types for titanic dataset" in {
    val columnsMeta = columnMetaDataComputer.compute(connector.getDataset(titanicDataSetSource), restrictions)

    columnsMeta(1).valueTypes should contain only("StringValue", "BooleanValue", "NumericValue", "IntValue", "HumanAgeValue")
    columnsMeta(1).uniqueValues should be(2)

    columnsMeta(4).valueTypes should contain only("StringValue", "GenderValue")

    columnsMeta(5).valueTypes should contain only("StringValue", "NumericValue", "HumanAgeValue")
  }

  it should "try to guess column types for income dataset" in {
    val columnsMeta = columnMetaDataComputer.compute(connector.getDataset(incomeDataSetSource), restrictions)

    columnsMeta(5).valueTypes should contain only("StringValue", "MaritalStatusValue")
  }

}
