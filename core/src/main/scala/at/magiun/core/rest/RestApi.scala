package at.magiun.core.rest

import com.twitter.finagle.Service
import com.twitter.finagle.http.filter.Cors
import com.twitter.finagle.http.{Request, Response, Status}
import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{Application, Output}

class RestApi(blockController: BlockController,
              dataSetController: DataSetController,
              executionController: ExecutionController
             ) extends LazyLogging {

  private val api = blockController.api :+:
    dataSetController.api :+:
    executionController.api

  private val service: Service[Request, Response] = api.handle({
    case e: Exception =>
      logger.error("Ooups! Something bad happened", e)
      Output.failure(e, Status.InternalServerError)
  })
    .toServiceAs[Application.Json]

  private  val policy: Cors.Policy = Cors.Policy(
    allowsOrigin = _ => Some("*"),
    allowsMethods = _ => Some(Seq("*")),
    allowsHeaders = _ => Some(Seq("*"))
  )

  val corsService: Service[Request, Response] = new Cors.HttpFilter(policy).andThen(service)

}
