package at.magiun.core.feature

import at.magiun.core.TestData.csvDataSetSource
import at.magiun.core.connector.CsvConnector
import at.magiun.core.{MainModule, UnitTest}

class FeatureRecommenderTest extends UnitTest {

  private val mainModule = new MainModule {}

  private val connector = new CsvConnector(mainModule.spark)

  it should "do something" in {
    val fr = new FeatureRecommender(mainModule.spark)
    val recommendation = fr.recommendFeatureOperation(connector.getDataset(csvDataSetSource))

    val map = recommendation.map
    map should have size 12
    map(0) should be(List.empty)
    map(1) should contain("BooleanColumn")
    map(3) should contain("NameColumn")
    map(4) should contain("GenderColumn")
    map(5) should contain("HumanAgeColumn")

  }

}
