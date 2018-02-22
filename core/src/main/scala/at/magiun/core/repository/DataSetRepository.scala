package at.magiun.core.repository

import at.magiun.core.model.SourceType
import slick.jdbc.H2Profile.api._
import slick.jdbc.meta.MTable

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class DataSetRepository(db: Database) {

  val TABLE_NAME = "DataSets"

  val dataSets = TableQuery[DataSets]

  private val tableExists = Await.result(db.run(MTable.getTables), 1.seconds).exists(_.name == TABLE_NAME)
  if (!tableExists) {
    Await.result(db.run(DBIO.seq(
      dataSets.schema.create
    )), 5.seconds)
  }

  def upsert(dataSet: MagiunDataSetEntity): Future[Int] = {
    val f = dataSets.insertOrUpdate(dataSet.id, dataSet.name, dataSet.sourceType.toString, dataSet.url)

    db.run(f)
  }

  def find(id: Long): Future[Option[MagiunDataSetEntity]] = {
    val action = dataSets.filter(_.id === id)
      .result
      .headOption

    db.run(action)
      .map(_.map(e =>
        MagiunDataSetEntity(e._1, e._2, SourceType.withName(e._3), e._4)
      ))
  }

  def findAll(): Future[Seq[MagiunDataSetEntity]] = {
    val all = dataSets.result

    db.run(all)
      .map(_.map(e =>
        MagiunDataSetEntity(e._1, e._2, SourceType.withName(e._3), e._4)
      ))
  }

  def delete(id: Long): Future[Int] = {
    val action = dataSets.filter(_.id === id)
      .delete

    db.run(action)
  }

  class DataSets(tag: Tag) extends Table[(Long, String, String, String)](tag, TABLE_NAME) {

    def id = column[Long]("ID", O.PrimaryKey)

    def name = column[String]("NAME")

    def sourceType = column[String]("SOURCE_TYPE")

    def url = column[String]("URL")

    override def * = (id, name, sourceType, url)

  }

}
