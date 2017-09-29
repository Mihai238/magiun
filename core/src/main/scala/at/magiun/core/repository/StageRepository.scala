package at.magiun.core.repository

import at.magiun.core.model.DropColumnStage
import slick.jdbc.H2Profile.api._

class StageRepository(db: Database) {

  val dropColumnStages = TableQuery[DropColumnStages]

  val setup = DBIO.seq(
    dropColumnStages.schema.create
  )

  db.run(setup)

  def getStage(id: Long): DropColumnStage = {
    val result = for {
      stage <- dropColumnStages if stage.id === id
    } yield (stage.id, stage.columnName)

    ???
  }

}

class DropColumnStages(tag: Tag) extends Table[(Long, String)](tag, "DropColumnStages") {

  def id = column[Long]("ID", O.PrimaryKey)
  def columnName = column[String]("COLUMN_NAME")

  override def * = (id, columnName)

}
