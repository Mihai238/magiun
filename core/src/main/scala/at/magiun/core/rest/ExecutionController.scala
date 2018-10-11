package at.magiun.core.rest

import at.magiun.core.model.{Execution, ExecutionResult}
import at.magiun.core.rest.FutureConverter._
import at.magiun.core.service.ExecutionService
import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.auto._
import io.finch.circe._
import io.finch.{Endpoint, _}

import scala.concurrent.ExecutionContext.Implicits.global

class ExecutionController(executionService: ExecutionService) extends LazyLogging {

  private val PATH = "executions"

  //noinspection TypeAnnotation
  lazy val api = upsertExecution

  val upsertExecution: Endpoint[ExecutionResult] = post(PATH :: jsonBody[Execution]) { execution: Execution =>
    logger.info(s"Creating execution '$execution'")

    executionService.execute(execution)
      .asTwitter
      .map(Ok)
  }

}
