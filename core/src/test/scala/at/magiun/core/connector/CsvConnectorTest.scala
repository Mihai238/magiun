package at.magiun.core.connector

import at.magiun.core.TestData._
import at.magiun.core.model.ColumnType
import at.magiun.core.{MainModule, UnitTest}

class CsvConnectorTest extends UnitTest {

  private val mainModule = new MainModule {}
  private val connector = new CsvConnector(mainModule.spark)

  it should "get schema given a source" in {
    val schema = connector.getSchema(csvDataSetSource)

    schema.columns should have size 18
    val column1 = schema.columns.head
    val column2 = schema.columns(1)

    column1.index should be (0)
    column1.name should be ("policyID")
    column1.`type` should be (ColumnType.Int)

    column2.index should be (1)
    column2.name should be ("statecode")
    column2.`type` should be (ColumnType.String)
  }

  it should "get rows given a source" in {
    val rows = connector.getRows(csvDataSetSource)

    rows should have size 15999
    rows.head.id should be (0)
    rows.head.values.head should be (119736)
    rows(6364).values(1) should be ("FL")
  }

  it should "get range of rows given a source" in {
    val rows = connector.getRows(csvDataSetSource, Range(10, 20))

    rows should have size 10
    rows.head.id should be (0)
    rows.head.values.head should be (253816)
  }

}
