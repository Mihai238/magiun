package at.magiun.core.repository

import slick.jdbc.H2Profile.api._
import slick.jdbc.meta.MTable

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class DataSetRepository(db: Database) {

  val TABLE_NAME = "DataSets"
  val dataSets = TableQuery[DataSets]

  if (db != null) {
    val tableExists = Await.result(db.run(MTable.getTables), 2.seconds).exists(_.name.name == TABLE_NAME)
    if (!tableExists) {
      Await.result(db.run(DBIO.seq(
        dataSets.schema.create
      )), 5.seconds)
    }
  }

  def upsert(dataSetEntity: MagiunDataSetEntity): Future[MagiunDataSetEntity] = {
    val action = (dataSets returning dataSets.map(_.id)).insertOrUpdate(dataSetEntity)

    db.run(action).map {
      case Some(id) => dataSetEntity.copy(id = id)
      case None => dataSetEntity
    }
  }

  def find(id: Long): Future[Option[MagiunDataSetEntity]] = {
    val action = dataSets.filter(_.id === id)
      .result
      .headOption

    db.run(action)
  }

  def findAll(): Future[Seq[MagiunDataSetEntity]] = {
    val all = dataSets.result

    db.run(all)
  }

  def delete(id: Long): Future[Int] = {
    val action = dataSets.filter(_.id === id)
      .delete

    db.run(action)
  }

  def deleteAll(): Future[Int] = {
    db.run(dataSets.delete)
  }

  class DataSets(tag: Tag) extends Table[MagiunDataSetEntity](tag, TABLE_NAME) {

    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def name = column[String]("NAME")

    def sourceType = column[String]("SOURCE_TYPE")

    def url = column[String]("URL")

    override def * = (id, name, sourceType, url) <> (MagiunDataSetEntity.tupled, MagiunDataSetEntity.unapply)

  }

}
