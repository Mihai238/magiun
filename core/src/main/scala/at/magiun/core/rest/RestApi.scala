package at.magiun.core.rest

import at.magiun.core.model.SourceType
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response, Status}
import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.auto._
import io.circe.{Encoder, Json}
import io.finch.circe._
import io.finch.{Application, Output}

class RestApi(userController: UserController,
              otherController: StageController,
              dataSetController: DataSetController) extends LazyLogging {

  private val api = userController.api :+:
    otherController.api :+:
    dataSetController.api

  val service: Service[Request, Response] = api.handle({
    case e: Exception =>
      logger.error("Ooups! Something bad happened", e)
      Output.failure(e, Status.InternalServerError)
  })
    .toServiceAs[Application.Json]


  // Encoders

  implicit val sourceTypeEncoderEncoder: Encoder[SourceType] = new Encoder[SourceType] {
    override def apply(sourceType: SourceType): Json = {
      Json.fromString(sourceType.toString)
    }
  }

}
