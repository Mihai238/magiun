package at.magiun.core.rest

import at.magiun.core.model.SourceType
import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import io.circe.generic.auto._
import io.circe.{Encoder, Json}
import io.finch.Application
import io.finch.circe._

class RestApi(userController: UserController,
              otherController: StageController,
              dataSetController: DataSetController) {

  private val api = userController.api :+:
    otherController.api :+:
    dataSetController.api

  val service: Service[Request, Response] = api.handle({
    case _ => ???
  })
    .toServiceAs[Application.Json]


  // Encoders

  implicit val sourceTypeEncoderEncoder: Encoder[SourceType] = new Encoder[SourceType] {
    override def apply(sourceType: SourceType): Json = {
      Json.fromString(sourceType.toString)
    }
  }

}
