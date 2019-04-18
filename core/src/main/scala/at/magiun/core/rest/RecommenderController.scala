package at.magiun.core.rest

import at.magiun.core.model.algorithm.Algorithm
import at.magiun.core.model.request.RecommenderRequest
import at.magiun.core.rest.FutureConverter._
import at.magiun.core.service.RecommenderService
import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._

import scala.concurrent.ExecutionContext.Implicits.global

class RecommenderController(recommenderService: RecommenderService) extends LazyLogging {

  private val BASE_PATH = "recommender"
  private val ALGORITHM_RECOMMENDATIONS_PATH = "algo-recommendations"

  //noinspection TypeAnnotation
  lazy val api = recommend

  val recommend: Endpoint[Set[Algorithm[_ <: Any]]] = post(BASE_PATH :: ALGORITHM_RECOMMENDATIONS_PATH :: jsonBody[RecommenderRequest]) {
    body: RecommenderRequest =>
      logger.info(body.toString)

      recommenderService.recommend(body)
        .asTwitter
        .map(_.get)
        .map(Ok)
  }

}
