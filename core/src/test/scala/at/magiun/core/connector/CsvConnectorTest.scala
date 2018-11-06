package at.magiun.core.connector

import at.magiun.core.TestData._
import at.magiun.core.model.ColumnType
import at.magiun.core.{MainModule, UnitTest}

class CsvConnectorTest extends UnitTest {

  private val mainModule = new MainModule {}
  private val connector = new CsvConnector(mainModule.spark)

  it should "get schema given a source" in {
    val schema = connector.getSchema(titanicDataSetSource)

    schema.columns should have size 12
    val column1 = schema.columns.head
    val column2 = schema.columns(3)

    column1.index should be (0)
    column1.name should be ("PassengerId")
    column1.`type` should be (ColumnType.Int)

    column2.index should be (3)
    column2.name should be ("Name")
    column2.`type` should be (ColumnType.String)
  }

  it should "get rows given a source" in {
    val rows = connector.getRows(titanicDataSetSource)

    rows should have size 891
    rows.head.id should be (0)
    rows.head.values(4) should be ("male")
    rows(30).values(1) should be ("0")
  }

  it should "get range of rows given a source" in {
    val rows = connector.getRows(titanicDataSetSource, Option(Range(10, 20)))

    rows should have size 10
    rows.head.id should be (0)
    rows.head.values.head should be ("11")
  }

  it should "get specific columns" in {
    val rows = connector.getRows(titanicDataSetSource, None, Option(Seq("PassengerId", "Survived", "Sex")))
    val firstRow = rows.head.values

    rows should have size 891
    rows.head.id should be (0)

    firstRow should have size 3
    firstRow(2) should be ("male")
  }

}
