package at.magiun.core.connector

import at.magiun.core.TestUtils._
import at.magiun.core.model.ColumnType
import at.magiun.core.{MainModule, UnitTest}

class CsvConnectorTest extends UnitTest {

  private val mainModule = new MainModule {}
  private val connector = new CsvConnector(mainModule.spark)

  private val sampleCsvPath = getClass.getClassLoader.getResource("insurance_sample.csv").getFile

  it should "get schema given an url" in {
    val schema = connector.getSchema(testDs1.copy(url = s"file://$sampleCsvPath"))

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

}
