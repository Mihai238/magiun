package at.magiun.core.statistics

import at.magiun.core.model.data.{DatasetMetadata, Distribution, VariableType}
import at.magiun.core.model.ontology.OntologyClass
import at.magiun.core.{MainModule, UnitTest}

class AlgorithmRecommenderTest extends UnitTest {

  private val mainModule = new MainModule {}
  private val algorithmRecommender = mainModule.algorithmRecommender
  private val spark = mainModule.spark
  private val ontology = mainModule.algorithmOntology

  it should s"recommend the ${OntologyClass.LinearLeastRegression.toString} for dataset metadata of a small dataset" in {
    val datasetMetadata = DatasetMetadata(
      VariableType.Continuous,
      Distribution.Normal,
      0.9,
      0.95,
      200,
      30,
      6000
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(3)
    recommendations should contain(OntologyClass.Algorithm)
    recommendations should contain(OntologyClass.Regression)
    recommendations should contain(OntologyClass.LinearLeastRegression)
  }

  it should s"not recommend ${OntologyClass.LinearLeastRegression.toString} for dataset with percentage of normal distribution < 0.8" in {
    val datasetMetadata = DatasetMetadata(
      VariableType.Continuous,
      Distribution.Normal,
      0.7,
      0.95,
      200,
      30,
      6000
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(2)
    recommendations shouldNot contain(OntologyClass.LinearLeastRegression)
  }

  it should s"not recommend ${OntologyClass.LinearLeastRegression.toString} for dataset with percentage of continuous variably tye < 0.8" in {
    val datasetMetadata = DatasetMetadata(
      VariableType.Continuous,
      Distribution.Normal,
      0.95,
      0.7,
      200,
      30,
      6000
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(2)
    recommendations shouldNot contain(OntologyClass.LinearLeastRegression)
  }

  it should s"not recommend ${OntologyClass.LinearLeastRegression.toString} for dataset with observation-variable ratio < 20" in {
    val datasetMetadata = DatasetMetadata(
      VariableType.Continuous,
      Distribution.Normal,
      0.95,
      0.7,
      200,
      30,
      6000
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(2)
    recommendations shouldNot contain(OntologyClass.LinearLeastRegression)
  }

  it should s"recommend the ${OntologyClass.LogisticRegression.toString} for dataset metadata of a small dataset" in {
    val datasetMetadata = DatasetMetadata(
      VariableType.Binary,
      Distribution.Binomial,
      0.9,
      0.95,
      90,
      30,
      2700
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(3)
    recommendations should contain(OntologyClass.Algorithm)
    recommendations should contain(OntologyClass.Regression)
    recommendations should contain(OntologyClass.LogisticRegression)
  }

  it should s"not recommend the ${OntologyClass.LogisticRegression} for dataset with response variable distribution different from ${OntologyClass.BinomialDistribution}" in {
    val datasetMetadata = DatasetMetadata(
      VariableType.Binary,
      Distribution.Exponential,
      0.9,
      0.95,
      90,
      30,
      2700
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(2)
    recommendations should contain(OntologyClass.Algorithm)
    recommendations should contain(OntologyClass.Regression)
    recommendations shouldNot contain(OntologyClass.LogisticRegression)
  }

  it should s"not recommend the ${OntologyClass.LogisticRegression} for dataset with response variable type different from ${OntologyClass.Binary}" in {
    val datasetMetadata = DatasetMetadata(
      VariableType.Continuous,
      Distribution.Binomial,
      0.9,
      0.95,
      90,
      30,
      2700
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(2)
    recommendations should contain(OntologyClass.Algorithm)
    recommendations should contain(OntologyClass.Regression)
    recommendations shouldNot contain(OntologyClass.LogisticRegression)
  }

  it should s"not recommend the ${OntologyClass.LogisticRegression} for dataset with insufficient observation-variable ratio" in {
    val datasetMetadata = DatasetMetadata(
      VariableType.Continuous,
      Distribution.Binomial,
      0.9,
      0.95,
      10,
      30,
      300
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(2)
    recommendations should contain(OntologyClass.Algorithm)
    recommendations should contain(OntologyClass.Regression)
    recommendations shouldNot contain(OntologyClass.LogisticRegression)
  }
}
