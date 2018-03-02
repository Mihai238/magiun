package at.magiun.core.rest

import at.magiun.core.model.Execution
import at.magiun.core.rest.FutureConverter._
import at.magiun.core.service.ExecutionService
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{Endpoint, _}

import scala.concurrent.ExecutionContext.Implicits.global

class ExecutionController(executionService: ExecutionService) {

  private val PATH = "executions"

  //noinspection TypeAnnotation
  lazy val api = persistExecution

  val persistExecution: Endpoint[Execution] = post(PATH :: jsonBody[Execution]) { execution: Execution =>
    executionService.execute(execution)
      .asTwitter
      .map(Ok)
  }

}
