package at.magiun.core.service

import at.magiun.core.model.ontology.OntologyClass._
import at.magiun.core.{MainModule, UnitTest}

class RecommendationsRankerTest extends UnitTest {

  private val mainModule = new MainModule {}
  private val ranker = mainModule.recommendatiosRanker

  it should "rank correctly the given algorithms" in {
    cancel("skipped")
    // given
    val recommendations = List(Algorithm, IsotonicRegression, GeneralizedLinearRegressionIdentityPartial, GeneralizedLinearRegressionIdentityComplete, SurvivalRegression)

    // when
    val rankedRecommendations = ranker.rank(recommendations)

    // then
    rankedRecommendations should equal (List(GeneralizedLinearRegressionIdentityComplete, IsotonicRegression, SurvivalRegression))
  }

  it should "rank correctly the given algorithms 2" in {
    cancel("skipped")
    // given
    val recommendations = List(Algorithm,
      IsotonicRegression,
      GeneralizedLinearRegressionIdentityPartial,
      GeneralizedLinearRegressionIdentityComplete,
      SurvivalRegression,
      LinearLeastRegressionPartial,
      RandomForestRegressionComplete
    )

    // when
    val rankedRecommendations = ranker.rank(recommendations)

    // then
    rankedRecommendations should equal (List(GeneralizedLinearRegressionIdentityComplete, LinearLeastRegressionPartial, RandomForestRegressionComplete, IsotonicRegression, SurvivalRegression))
  }

}
