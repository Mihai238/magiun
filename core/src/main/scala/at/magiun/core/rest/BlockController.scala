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
  lazy val api = getBlock :+: upsertBlock

  val getBlock: Endpoint[Block] = get(PATH :: path[String]) { id: String =>
    blockService.find(id)
      .asTwitter
      .map {
        case Some(block) => Ok(block)
        case None => NotFound(new RuntimeException("Block not found"))
      }
  }

  val upsertBlock: Endpoint[Block] = post(PATH :: jsonBody[Block]) { block: Block =>
    logger.info("Creating new block")

    blockService.upsert(block)
      .asTwitter
      .map(Ok)
  }

}
