package at.magiun.core.feature

import at.magiun.core.TestData._
import at.magiun.core.connector.CsvConnector
import at.magiun.core.{MainModule, UnitTest}

class RecommenderTest extends UnitTest {

  private val mainModule = new MainModule {}

  private val connector = new CsvConnector(mainModule.spark)
  private val recommender = mainModule.recommender

  it should "predict for titanic dataset" in {
    val recommendation = recommender.recommend(connector.getDataset(titanicDataSetSource)).map

    recommendation should have size 12
    recommendation(0).colTypes should be(List.empty)
    recommendation(1).colTypes should contain only("BooleanColumn", "CategoricalColumn")
//    recommendation(3).colTypes should contain only("NameColumn")
    recommendation(4).colTypes should contain only("GenderColumn", "CategoricalColumn")
    recommendation(5).colTypes should contain only "HumanAgeColumn"
//    recommendation(5).operations should contain only("DiscretizationSuitable")
  }

  it should "predict for income dataset" in {
    val recommendation = recommender.recommend(connector.getDataset(incomeDataSetSource)).map

    recommendation(0).colTypes should contain only "HumanAgeColumn"
    recommendation(5).colTypes should contain only ("MaritalStatusColumn", "CategoricalColumn")
  }

}
