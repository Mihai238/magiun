package at.magiun.core.rest

import com.twitter.finagle.Service
import com.twitter.finagle.http.{Request, Response}
import io.circe.generic.auto._
import io.finch.Application
import io.finch.circe._

class RestApi(userController: UserController, otherController: OtherController) {

  private val api = userController.api :+:
    otherController.api

  val service: Service[Request, Response] = api.handle({
    case _ => ???
  })
    .toServiceAs[Application.Json]

}
