package at.magiun.core.feature

import at.magiun.core.TestData.csvDataSetSource
import at.magiun.core.connector.CsvConnector
import at.magiun.core.{MainModule, UnitTest}

class RecommenderTest extends UnitTest {

  private val mainModule = new MainModule {}

  private val connector = new CsvConnector(mainModule.spark)
  private val recommender = mainModule.recommender

  it should "try to guess column types" in {
    val recommendation = recommender.recommendFeatureOperation(connector.getDataset(csvDataSetSource))

    val map = recommendation.map
    map should have size 12
    map(0).colTypes should be(List.empty)
    map(1).colTypes should contain("BooleanColumn")
    map(3).colTypes should contain("NameColumn")
    map(4).colTypes should contain("GenderColumn")
    map(5).colTypes should contain("HumanAgeColumn")
    map(5).operations should contain("Discretization")

  }

}
