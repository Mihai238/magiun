package at.magiun.core.feature

import at.magiun.core.TestData.titanicDataSetSource
import at.magiun.core.connector.CsvConnector
import at.magiun.core.{MainModule, UnitTest}

class ColumnTypeRecommenderTest extends UnitTest {

  private val mainModule = new MainModule {}

  private val connector = new CsvConnector(mainModule.spark)
  private val restrictionBuilder = mainModule.restrictionBuilder
  private val valueTypeComputer = mainModule.valueTypeComputer
  private val columnTypeRecommender = mainModule.columnTypeRecommender

  it should "" in {
    val restrictions = restrictionBuilder.build(mainModule.model)
    val valueTypes = valueTypeComputer.process(connector.getDataset(titanicDataSetSource), restrictions)
    val columnTypes = columnTypeRecommender.f(valueTypes)
//    columnTypes(0) should be(List.empty)
    columnTypes(1) should contain("BooleanColumn")
//    columnTypes(3) should contain("NameColumn")
    columnTypes(4) should contain("GenderColumn")
    columnTypes(5) should contain("HumanAgeColumn")
  }

}
