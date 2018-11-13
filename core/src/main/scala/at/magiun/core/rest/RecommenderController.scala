package at.magiun.core.rest

import at.magiun.core.model.request.RecommenderRequestBody
import at.magiun.core.service.RecommenderService
import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._

class RecommenderController(recommenderService: RecommenderService) extends LazyLogging {

  private val BASE_PATH = "recommender"
  private val ALGORITHM_RECOMMENDATIONS_PATH = "algo-recommendations"

  //noinspection TypeAnnotation
  lazy val api = recommend

  val recommend: Endpoint[String] = post(BASE_PATH :: ALGORITHM_RECOMMENDATIONS_PATH :: jsonBody[RecommenderRequestBody]) { body: RecommenderRequestBody =>
    logger.info(body.toString)

    recommenderService.recommend(body)

    Ok("ok")
  }

}
