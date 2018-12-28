package at.magiun.core.service

import java.util.concurrent.atomic.AtomicInteger

import at.magiun.core.model.{Block, BlockInput, BlockType}
import at.magiun.core.repository.{BlockEntity, BlockRepository}
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.syntax._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class BlockService(blockRepository: BlockRepository) {

  private val idGenerator = new AtomicInteger(100)

  case class Config(inputs: Seq[BlockInput], params: Map[String, String])

  def find(id: String): Future[Block] = {
    blockRepository.find(id)
      .map(mapToModel)
  }

  def upsert(block: Block): Future[Block] = {
    blockRepository.upsert(mapToEntity(block))
      .map(mapToModel)
  }

  def delete(id: String): Future[Int] = {
    blockRepository.delete(id)
  }

  private def mapToModel(entity: BlockEntity): Block = {
    val config = decode[Config](entity.config) match {
      case Left(e) => throw new RuntimeException(e)
      case Right(c) => c
    }

    Block(entity.id, BlockType.withName(entity.`type`), config.inputs, config.params)
  }

  private def mapToEntity(block: Block): BlockEntity = {
    val config = Config(block.inputs, block.params).asJson.noSpaces
    val id = if (block.id.isEmpty) {
      s"block-${idGenerator.getAndIncrement()}"
    } else {
      block.id
    }
    BlockEntity(id, block.`type`.toString, config)
  }

}
