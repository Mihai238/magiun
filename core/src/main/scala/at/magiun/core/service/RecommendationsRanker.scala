package at.magiun.core.service

import at.magiun.core.model.ontology.OntologyClass
import scala.util.Random

class RecommendationsRanker {

  def rank(recommendations: List[OntologyClass]): List[OntologyClass] = {
    val algorithms = recommendations.filterNot(OntologyClass.isAbstractType)
    val partialAlgorithms = algorithms.filter(OntologyClass.isCompleteAlgorithm).map(OntologyClass.getPartialOfCompleteAlgorithm)

    val cleanedAlgorithms = algorithms.filterNot(partialAlgorithms.contains)
      .sortBy(OntologyClass.isPartialAlgorithm)
      .sortBy(OntologyClass.isGenericAlgorithm)
      .sortBy(OntologyClass.isSpecialCaseAlgorithm)

    Random.shuffle(cleanedAlgorithms)
  }
}
