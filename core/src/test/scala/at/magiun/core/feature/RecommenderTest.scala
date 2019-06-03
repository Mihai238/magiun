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
    recommendation(0).colTypes should contain only "QuantitativeColumn"

    recommendation(1).colTypes should contain only "BooleanColumn"
    recommendation(1).operations should be(List.empty)

    recommendation(3).colTypes should be(List.empty)
    recommendation(3).operations should contain only "ExtractTitleSuitableColumn"

    recommendation(4).colTypes should contain only "GenderColumn"
    recommendation(4).operations should be(List.empty)

    recommendation(5).colTypes should contain only("HumanAgeColumn", "QuantitativeColumn")
    recommendation(5).operations should contain only("DiscretizationSuitableColumn", "HandleMissingValuesSuitableColumn")
  }

  it should "predict for income dataset" in {
    val recommendation = recommender.recommend(connector.getDataset(incomeDataSetSource)).map

    recommendation(0).colTypes should contain only("QuantitativeColumn", "HumanAgeColumn")

    recommendation(5).colTypes should contain only "MaritalStatusColumn"

    recommendation(8).colTypes should contain only "HumanEthnicityColumn"

    recommendation(13).colTypes should contain only "CountryColumn"
  }

}
