package at.magiun.core.service

import at.magiun.core.model.{MagiunDataSet, Schema}
import at.magiun.core.repository.{DataSetRepository, MagiunDataSetEntity}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DataSetService(dataSetRepository: DataSetRepository) {

  def find(id: Long): Future[Option[MagiunDataSet]] = {
    dataSetRepository.find(id)
      .map(_.map(mapToModel))
  }

  def findAll(): Future[Seq[MagiunDataSet]] = {
    dataSetRepository.findAll()
      .map(_.map(mapToModel))
  }

  def mapToModel(entity: MagiunDataSetEntity): MagiunDataSet = {
    MagiunDataSet(
      entity.id,
      entity.name,
      entity.sourceType,
      entity.url,
      Schema(List())
    )
  }

}
