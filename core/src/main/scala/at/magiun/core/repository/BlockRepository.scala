package at.magiun.core.repository

import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class BlockRepository(db: Database) {

  val TABLE_NAME = "Blocks"
  val blocks = TableQuery[Blocks]

  if (db != null) {
    Await.result(db.run(DBIO.seq(blocks.schema.create)), 5.seconds)
  }

  def upsert(block: BlockEntity): Future[BlockEntity] = {
    val action = blocks.insertOrUpdate(block)

    db.run(action).map(_ => block)
  }

  def find(id: String): Future[BlockEntity] = {
    val action = blocks.filter(_.id === id)
      .result
      .head

    db.run(action)
  }

  class Blocks(tag: Tag) extends Table[BlockEntity](tag, TABLE_NAME) {

    def id = column[String]("ID", O.PrimaryKey)

    def `type` = column[String]("TYPE")

    def config = column[String]("CONFIG")

    override def * = (id, `type`, config) <> (BlockEntity.tupled, BlockEntity.unapply)

  }

}