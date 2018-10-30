package at.magiun.core.feature

import at.magiun.core.TestData._
import at.magiun.core.connector.CsvConnector
import at.magiun.core.{MainModule, UnitTest}

class ColumnTypeRecommenderTest extends UnitTest {

  private val mainModule = new MainModule {}

  private val connector = new CsvConnector(mainModule.spark)
  private val restrictionBuilder = mainModule.restrictionBuilder
  private val columnMetaDataComputer = mainModule.columnMetaDataComputer
  private val columnTypeRecommender = mainModule.columnTypeRecommender

  private val restrictions = restrictionBuilder.build(mainModule.model)

  it should "predict for titanic dataset" in {
    val columnsMetaData = columnMetaDataComputer.compute(connector.getDataset(titanicDataSetSource), restrictions)
    val columnTypes = columnTypeRecommender.recommend(columnsMetaData)
    columnTypes(0) should contain only "Column"
    columnTypes(1) should contain only ("BooleanColumn", "CategoricalColumn", "Column")
//    columnTypes(3) should contain("NameColumn")
    columnTypes(4) should contain only ("GenderColumn", "CategoricalColumn", "Column")
    columnTypes(5) should contain only ("HumanAgeColumn", "Column")
  }

  it should "predict for income dataset" in {
    val columnsMetaData = columnMetaDataComputer.compute(connector.getDataset(incomeDataSetSource), restrictions)
    val columnTypes = columnTypeRecommender.recommend(columnsMetaData)

    columnTypes(0) should contain only ("HumanAgeColumn", "Column")
    columnTypes(5) should contain only ("MaritalStatusColumn", "CategoricalColumn", "Column")
  }

}
