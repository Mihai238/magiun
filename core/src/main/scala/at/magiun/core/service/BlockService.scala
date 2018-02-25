package at.magiun.core.service

import at.magiun.core.model.{Block, BlockType}
import at.magiun.core.repository.BlockRepository

import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

class BlockService(blockRepository: BlockRepository) {

  def find(id: Int): Future[Option[Block]] = {
    // TODO
    Future(Option(Block("2", BlockType.FileReader, Seq(("1", 0)), params = Map("x" -> "4"))))
  }

  def create(block: Block): Future[Block] = {
    ???
  }

}
