package at.magiun.core.rest

import at.magiun.core.model.Block
import at.magiun.core.rest.FutureConverter._
import at.magiun.core.service.BlockService
import com.typesafe.scalalogging.LazyLogging
import io.circe.generic.auto._
import io.finch._
import io.finch.circe._

import scala.concurrent.ExecutionContext.Implicits.global

class BlockController(blockService: BlockService) extends LazyLogging {

  private val PATH = "blocks"

  //noinspection TypeAnnotation
  lazy val api = getBlock

  val getBlock: Endpoint[Block] = get(PATH :: path[Int]) { id: Int =>
    blockService.find(id)
      .map(e => e.get)
      .asTwitter
      .map(Ok)
  }

  val createBlock: Endpoint[Block] = post(PATH :: jsonBody[Block]) { block: Block =>
    logger.info("Creating new block")

    blockService.create(block)
      .asTwitter
      .map(Ok)
  }

}
