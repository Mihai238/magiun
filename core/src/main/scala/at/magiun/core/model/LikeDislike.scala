package at.magiun.core.model

import at.magiun.core.model.algorithm.Algorithm
import at.magiun.core.model.rest.request.RecommenderRequest
import org.apache.spark.ml.{Estimator, Model}

case class LikeDislike(like: Boolean, request: RecommenderRequest, recommendation: Algorithm[_ <: Estimator[_ <: Model[_ <: Model[_]]]]) extends Serializable
