package at.magiun.core.repository

import at.magiun.core.model.DropColumnBlock
import slick.jdbc.H2Profile.api._

class BlockRepository(db: Database) {

  val dropColumnBlocks = TableQuery[DropColumnBlocks]

  val setup = DBIO.seq(
    dropColumnBlocks.schema.create
  )

  db.run(setup)

  def getBlock(id: Long): DropColumnBlock = {
    val result = for {
      block <- dropColumnBlocks if block.id === id
    } yield (block.id, block.columnName)

    ???
  }

}

class DropColumnBlocks(tag: Tag) extends Table[(Long, String)](tag, "DropColumnBlocks") {

  def id = column[Long]("ID", O.PrimaryKey)
  def columnName = column[String]("COLUMN_NAME")

  override def * = (id, columnName)

}
