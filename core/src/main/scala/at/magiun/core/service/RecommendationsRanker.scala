package at.magiun.core.service

import at.magiun.core.model.ontology.OntologyClass

class RecommendationsRanker {

  def rank(recommendations: Set[OntologyClass]): Set[OntologyClass] = {
    recommendations.filterNot(OntologyClass.isAbstractType)
  }

}
