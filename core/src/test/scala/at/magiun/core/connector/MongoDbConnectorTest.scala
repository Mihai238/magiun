package at.magiun.core.connector

import at.magiun.core.model.ColumnType
import at.magiun.core.{MainModule, TestData, UnitTest}

class MongoDbConnectorTest extends UnitTest {

  private val mainModule = new MainModule {}
  private val connector = new MongoDbConnector(mainModule.spark)

  // needs a running mongodb
  ignore should "get schema given an url" in {
    val schema = connector.getSchema(TestData.mongoDataSource)

    schema.columns should have size 5
    val col1 = schema.columns.head
    val col2 = schema.columns(1)

    col1.name should be ("_id")
    col1.`type` should be (ColumnType.Unknown)

    col2.name should be ("alcohol")
    col2.`type` should be (ColumnType.Double)
  }

}
