package at.magiun.core.model.rest.response

import at.magiun.core.model.algorithm.Algorithm
import org.apache.spark.ml.Estimator

case class RecommenderResponse(
                              requestId: String,
                              recommendations: Seq[Algorithm[_ <: Estimator[_ <: Any]]],
                              nonRecommendations: Seq[Algorithm[_ <: Estimator[_ <: Any]]],
                              message: String = ""
                              ) {

}
