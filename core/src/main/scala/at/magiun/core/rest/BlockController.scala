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
  private val UPSERT = "upsert"
  private val DELETE = "delete"

  //noinspection TypeAnnotation
  lazy val api = getBlock :+: upsertBlock

  val getBlock: Endpoint[Block] = get(PATH :: path[String]) { id: String =>
    blockService.find(id)
      .asTwitter
      .map(Ok)
  }

  val upsertBlock: Endpoint[Block] = post(PATH :: UPSERT :: jsonBody[Block]) { block: Block =>
    logger.info("Upserting a block")

    blockService.upsert(block)
      .asTwitter
      .map(Ok)
  }

  val deleteBlock: Endpoint[Int] = delete(PATH :: DELETE :: path[String]) { id: String =>
    logger.info(s"Deleting block $id")
    blockService.delete(id)
      .asTwitter
      .map(Ok)
  }

}
