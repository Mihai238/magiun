package at.magiun.core.statistics

import at.magiun.core.model.algorithm.AlgorithmGoal
import at.magiun.core.model.data.{DatasetMetadata, Distribution, VariableType}
import at.magiun.core.model.ontology.OntologyClass
import at.magiun.core.{MainModule, UnitTest}

class AlgorithmRecommenderTest extends UnitTest {

  private val mainModule = new MainModule {}
  private val algorithmRecommender = mainModule.algorithmRecommender

  it should s"recommend ${OntologyClass.LinearLeastRegressionPartial.name} for dataset metadata of a small dataset" in {
    val datasetMetadata = DatasetMetadata(
      AlgorithmGoal.GoalRegression,
      VariableType.Continuous,
      Distribution.Normal,
      0.9,
      0,
      0,
      0.95,
      0,
      0,
      200,
      multicollinearity = false
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(12)
    recommendations should contain(OntologyClass.Algorithm)
    recommendations should contain(OntologyClass.Regression)
    recommendations should contain(OntologyClass.LinearLeastRegressionPartial)
    recommendations should contain(OntologyClass.GeneralizedLinearRegressionPartial)
    recommendations should contain(OntologyClass.GradientBoostTreeRegressionComplete)
    recommendations should contain(OntologyClass.GradientBoostTreeRegressionPartial)
    recommendations should contain(OntologyClass.IsotonicRegression)
    recommendations should contain(OntologyClass.SurvivalRegression)
    recommendations should contain(OntologyClass.DecisionTreeRegressionPartial)
    recommendations should contain(OntologyClass.DecisionTreeRegressionComplete)
    recommendations should contain(OntologyClass.RandomForestRegressionPartial)
    recommendations should contain(OntologyClass.RandomForestRegressionComplete)
  }

  it should s"not recommend ${OntologyClass.LinearLeastRegressionPartial.name} for dataset with percentage of normal distribution < 0.7" in {
    val datasetMetadata = DatasetMetadata(
      AlgorithmGoal.GoalRegression,
      VariableType.Continuous,
      Distribution.Normal,
      0.6,
      0,
      0,
      0.95,
      0,
      0,
      200,
      multicollinearity = false
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(11)
    recommendations shouldNot contain(OntologyClass.LinearLeastRegressionPartial)
    recommendations should contain(OntologyClass.Regression)
    recommendations should contain(OntologyClass.GeneralizedLinearRegressionPartial)
    recommendations should contain(OntologyClass.SurvivalRegression)
    recommendations should contain(OntologyClass.GradientBoostTreeRegressionPartial)
    recommendations should contain(OntologyClass.RandomForestRegressionPartial)
    recommendations should contain(OntologyClass.RandomForestRegressionComplete)
    recommendations should contain(OntologyClass.DecisionTreeRegressionPartial)
    recommendations should contain(OntologyClass.DecisionTreeRegressionComplete)
  }

  it should s"not recommend ${OntologyClass.LinearLeastRegressionPartial.name} for dataset with percentage of continuous variably tye < 0.7" in {
    val datasetMetadata = DatasetMetadata(
      AlgorithmGoal.GoalRegression,
      VariableType.Continuous,
      Distribution.Normal,
      0.95,
      0,
      0,
      0.6,
      0,
      0,
      200,
      multicollinearity = false
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(9)
    recommendations shouldNot contain(OntologyClass.LinearLeastRegressionPartial)
    recommendations should contain(OntologyClass.Algorithm)
    recommendations should contain(OntologyClass.Regression)
    recommendations should contain(OntologyClass.GeneralizedLinearRegressionPartial)
    recommendations should contain(OntologyClass.GradientBoostTreeRegressionPartial)
    recommendations should contain(OntologyClass.GradientBoostTreeRegressionComplete)
    recommendations should contain(OntologyClass.RandomForestRegressionPartial)
    recommendations should contain(OntologyClass.RandomForestRegressionComplete)
    recommendations should contain(OntologyClass.DecisionTreeRegressionPartial)
    recommendations should contain(OntologyClass.DecisionTreeRegressionComplete)
  }

  it should s"not recommend ${OntologyClass.LinearLeastRegressionPartial.name} for dataset with observation-variable ratio < 20" in {
    val datasetMetadata = DatasetMetadata(
      AlgorithmGoal.GoalRegression,
      VariableType.Continuous,
      Distribution.Normal,
      0.95,
      0,
      0,
      0.7,
      0,
      0,
      10,
      multicollinearity = false
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(2)
    recommendations shouldNot contain(OntologyClass.LinearLeastRegressionPartial)
    recommendations shouldNot contain(OntologyClass.GeneralizedLinearRegressionPartial)
  }

  it should s"recommend ${OntologyClass.BinaryLogisticRegressionPartial.name} for dataset metadata of a small dataset" in {
    val datasetMetadata = DatasetMetadata(
      AlgorithmGoal.GoalRegression,
      VariableType.Binary,
      Distribution.Bernoulli,
      0.9,
      0,
      0,
      0.95,
      0,
      0,
      120,
      multicollinearity = false
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(4)
    recommendations should contain(OntologyClass.Algorithm)
    recommendations should contain(OntologyClass.Regression)
    recommendations should contain(OntologyClass.BinaryLogisticRegressionPartial)
    recommendations should contain(OntologyClass.GeneralizedLinearRegressionPartial)
  }

  it should s"recommend ${OntologyClass.GradientBoostTreeClassification.name} for dataset metadata of a small dataset" in {
    val datasetMetadata = DatasetMetadata(
      AlgorithmGoal.GoalClassification,
      VariableType.Binary,
      Distribution.Normal,
      1,
      0,
      0,
      1,
      0,
      0,
      120,
      multicollinearity = false
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(9)
    recommendations should contain(OntologyClass.Algorithm)
    recommendations should contain(OntologyClass.Classification)
    recommendations should contain(OntologyClass.LinearSupportVectorMachine)
    recommendations should contain(OntologyClass.DecisionTreeClassificationPartial)
    recommendations should contain(OntologyClass.DecisionTreeClassificationComplete)
    recommendations should contain(OntologyClass.RandomForestClassificationPartial)
    recommendations should contain(OntologyClass.RandomForestClassificationComplete)
    recommendations should contain(OntologyClass.GradientBoostTreeClassification)
    recommendations should contain(OntologyClass.MultilayerPerceptronClassification)
  }

  it should s"not recommend ${OntologyClass.MultilayerPerceptronClassification.name} for dataset metadata of a small dataset with multicollinearity" in {
    val datasetMetadata = DatasetMetadata(
      AlgorithmGoal.GoalClassification,
      VariableType.Binary,
      Distribution.Normal,
      1,
      0,
      0,
      1,
      0,
      0,
      120,
      multicollinearity = true
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(6)
    recommendations should contain(OntologyClass.Algorithm)
    recommendations should contain(OntologyClass.Classification)
    recommendations should contain(OntologyClass.LinearSupportVectorMachine)
    recommendations should contain(OntologyClass.DecisionTreeClassificationPartial)
    recommendations should contain(OntologyClass.RandomForestClassificationPartial)
    recommendations should contain(OntologyClass.MultilayerPerceptronClassification)
  }

  it should s"recommend ${OntologyClass.BernoulliNaiveBayesClassification.name} for dataset metadata of a small dataset" in {
    val datasetMetadata = DatasetMetadata(
      AlgorithmGoal.GoalClassification,
      VariableType.Binary,
      Distribution.Bernoulli,
      0,
      1,
      0,
      0,
      1,
      0,
      120,
      multicollinearity = false
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(10)
    recommendations should contain(OntologyClass.Algorithm)
    recommendations should contain(OntologyClass.Classification)
    recommendations should contain(OntologyClass.LinearSupportVectorMachine)
    recommendations should contain(OntologyClass.DecisionTreeClassificationPartial)
    recommendations should contain(OntologyClass.DecisionTreeClassificationComplete)
    recommendations should contain(OntologyClass.BernoulliNaiveBayesClassification)
    recommendations should contain(OntologyClass.RandomForestClassificationPartial)
    recommendations should contain(OntologyClass.RandomForestClassificationComplete)
    recommendations should contain(OntologyClass.GradientBoostTreeClassification)
    recommendations should contain(OntologyClass.MultilayerPerceptronClassification)
  }

  it should s"not recommend ${OntologyClass.BernoulliNaiveBayesClassification.name} for dataset metadata of a small dataset with multicollinearity" in {
    val datasetMetadata = DatasetMetadata(
      AlgorithmGoal.GoalClassification,
      VariableType.Binary,
      Distribution.Bernoulli,
      0,
      1,
      0,
      0,
      1,
      0,
      120,
      multicollinearity = true
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(6)
    recommendations should contain(OntologyClass.Algorithm)
    recommendations should contain(OntologyClass.Classification)
    recommendations should contain(OntologyClass.LinearSupportVectorMachine)
    recommendations should contain(OntologyClass.DecisionTreeClassificationPartial)
    recommendations should contain(OntologyClass.RandomForestClassificationPartial)
    recommendations should contain(OntologyClass.MultilayerPerceptronClassification)
    recommendations shouldNot contain(OntologyClass.BernoulliNaiveBayesClassification)
  }

  it should s"recommend ${OntologyClass.MultinomialNaiveBayesClassification.name} for dataset metadata of a small dataset" in {
    val datasetMetadata = DatasetMetadata(
      AlgorithmGoal.GoalClassification,
      VariableType.Binary,
      Distribution.Bernoulli,
      0,
      0,
      1,
      0,
      0,
      1,
      120,
      multicollinearity = false
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(10)
    recommendations should contain(OntologyClass.Algorithm)
    recommendations should contain(OntologyClass.Classification)
    recommendations should contain(OntologyClass.LinearSupportVectorMachine)
    recommendations should contain(OntologyClass.DecisionTreeClassificationPartial)
    recommendations should contain(OntologyClass.DecisionTreeClassificationComplete)
    recommendations should contain(OntologyClass.MultinomialNaiveBayesClassification)
    recommendations should contain(OntologyClass.RandomForestClassificationPartial)
    recommendations should contain(OntologyClass.RandomForestClassificationComplete)
    recommendations should contain(OntologyClass.GradientBoostTreeClassification)
    recommendations should contain(OntologyClass.MultilayerPerceptronClassification)
  }

  it should s"not recommend ${OntologyClass.MultinomialNaiveBayesClassification.name} for dataset metadata of a small dataset with multicollinearity" in {
    val datasetMetadata = DatasetMetadata(
      AlgorithmGoal.GoalClassification,
      VariableType.Binary,
      Distribution.Bernoulli,
      0,
      0,
      1,
      0,
      0,
      1,
      120,
      multicollinearity = true
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(6)
    recommendations should contain(OntologyClass.Algorithm)
    recommendations should contain(OntologyClass.Classification)
    recommendations should contain(OntologyClass.LinearSupportVectorMachine)
    recommendations should contain(OntologyClass.DecisionTreeClassificationPartial)
    recommendations should contain(OntologyClass.RandomForestClassificationPartial)
    recommendations should contain(OntologyClass.MultilayerPerceptronClassification)
    recommendations shouldNot contain(OntologyClass.MultinomialNaiveBayesClassification)
  }

    it should s"not recommend ${OntologyClass.BinaryLogisticRegressionPartial.name} for dataset with response variable distribution different from ${OntologyClass.BernoulliDistribution}" in {
    val datasetMetadata = DatasetMetadata(
      AlgorithmGoal.GoalRegression,
      VariableType.Binary,
      Distribution.Exponential,
      0.9,
      0,
      0,
      0.95,
      0,
      0,
      90,
      multicollinearity = false
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(2)
    recommendations should contain(OntologyClass.Algorithm)
    recommendations should contain(OntologyClass.Regression)
    recommendations shouldNot contain(OntologyClass.BinaryLogisticRegressionPartial)
  }

  it should s"not recommend ${OntologyClass.BinaryLogisticRegressionPartial.name} for dataset with response variable type different from ${OntologyClass.Binary}" in {
    val datasetMetadata = DatasetMetadata(
      AlgorithmGoal.GoalRegression,
      VariableType.Continuous,
      Distribution.Binomial,
      0.9,
      0,
      0,
      0.95,
      0,
      0,
      90,
      multicollinearity = true
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(7)
    recommendations should contain(OntologyClass.Algorithm)
    recommendations should contain(OntologyClass.Regression)
    recommendations should contain(OntologyClass.SurvivalRegression)
    recommendations should contain(OntologyClass.IsotonicRegression)
    recommendations should contain(OntologyClass.GradientBoostTreeRegressionPartial)
    recommendations should contain(OntologyClass.RandomForestRegressionPartial)
    recommendations shouldNot contain(OntologyClass.BinaryLogisticRegressionPartial)
  }

  it should s"not recommend ${OntologyClass.BinaryLogisticRegressionPartial.name} for dataset with insufficient observation-variable ratio" in {
    val datasetMetadata = DatasetMetadata(
      AlgorithmGoal.GoalRegression,
      VariableType.Continuous,
      Distribution.Binomial,
      0.9,
      0,
      0,
      0.95,
      0,
      0,
      10,
      multicollinearity = false
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(2)
    recommendations should contain(OntologyClass.Algorithm)
    recommendations should contain(OntologyClass.Regression)
    recommendations shouldNot contain(OntologyClass.BinaryLogisticRegressionPartial)
  }
}
