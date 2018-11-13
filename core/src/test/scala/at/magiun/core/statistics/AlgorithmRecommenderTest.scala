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
      Seq.fill(2)(VariableType.Continuous),
      Seq.fill(2)(Distribution.Normal),
      0,
      Seq(),
      2,
      3000
    )

    val recommendations = algorithmRecommender.recommend(datasetMetadata)

    recommendations.size should be(3)
    recommendations should contain(OntologyClass.Algorithm)
    recommendations should contain(OntologyClass.Regression)
    recommendations should contain(OntologyClass.LinearLeastRegression)
  }
}
