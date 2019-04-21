package at.magiun.core.rest

import at.magiun.core.model.algorithm.Algorithm
import at.magiun.core.model.request.{RecommenderRequest, TrainAlgorithmRequest}
import at.magiun.core.model.response.TrainAlgorithmResponse
import at.magiun.core.rest.FutureConverter._
import at.magiun.core.service.{AlgorithmService, RecommenderService}
import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._
import org.apache.spark.ml.Estimator

import scala.concurrent.ExecutionContext.Implicits.global

class AlgorithmController(recommenderService: RecommenderService, algorithmService: AlgorithmService) extends LazyLogging {

  private val BASE_PATH = "algorithm"
  private val ALGORITHM_RECOMMENDATIONS_PATH = "recommend"
  private val TRAIN_ALGORITHM_PATH = "train"

  //noinspection TypeAnnotation
  lazy val api = recommend :+: train

  val recommend: Endpoint[Set[Algorithm[_ <: Estimator[_ <: Any]]]] = post(BASE_PATH :: ALGORITHM_RECOMMENDATIONS_PATH :: jsonBody[RecommenderRequest]) {
    body: RecommenderRequest =>
      logger.info(body.toString)

      recommenderService.recommend(body)
        .asTwitter
        .map(_.get)
        .map(Ok)
  }

  val train: Endpoint[TrainAlgorithmResponse] = post(BASE_PATH :: TRAIN_ALGORITHM_PATH :: jsonBody[TrainAlgorithmRequest]) {
    body: TrainAlgorithmRequest =>
      logger.info(body.toString)

      algorithmService.train(body)
        .asTwitter
        .map(_.get)
        .map(Ok)
  }

}
