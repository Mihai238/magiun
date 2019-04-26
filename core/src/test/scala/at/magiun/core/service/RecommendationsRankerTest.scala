package at.magiun.core.service

import at.magiun.core.model.ontology.OntologyClass._
import at.magiun.core.{MainModule, UnitTest}

class RecommendationsRankerTest extends UnitTest {

  private val mainModule = new MainModule {}
  private val ranker = mainModule.recommendatiosRanker

  it should "rank correctly the given algorithms" in {
    // given
    val recommendations = List(Algorithm, IsotonicRegression, GeneralizedLinearRegressionPartial, GeneralizedLinearRegressionComplete, SurvivalRegression)

    // when
    val rankedRecommendations = ranker.rank(recommendations)

    // then
    rankedRecommendations should equal (List(GeneralizedLinearRegressionComplete, IsotonicRegression, SurvivalRegression))
  }

  it should "rank correctly the given algorithms 2" in {
    // given
    val recommendations = List(Algorithm,
      IsotonicRegression,
      GeneralizedLinearRegressionPartial,
      GeneralizedLinearRegressionComplete,
      SurvivalRegression,
      LinearLeastRegressionPartial,
      RandomForestRegressionComplete
    )

    // when
    val rankedRecommendations = ranker.rank(recommendations)

    // then
    rankedRecommendations should equal (List(GeneralizedLinearRegressionComplete, LinearLeastRegressionPartial, RandomForestRegressionComplete, IsotonicRegression, SurvivalRegression))
  }

}
