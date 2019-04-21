package at.magiun.core.util

import at.magiun.core.model.{MagiunDataSet, Schema}

object MagiunDatasetUtil {

  def cleanDatasetFromUnnecessaryVariables(magiunDataset: MagiunDataSet, responseVariable: Int, explanatoryVariables: Seq[Int]): MagiunDataSet = {
    val columns = magiunDataset.schema.get.columns
    val columnsToRemove: Seq[String] = columns.indices
      .filter(i => !(explanatoryVariables.contains(i) || i == responseVariable))
      .map(i => columns(i).name)

    val cleanColumns = columnsToRemove.foldLeft(magiunDataset.schema.get.columns)((l, r) => l.filterNot(_.name == r))

    MagiunDataSet(magiunDataset.id, magiunDataset.name, magiunDataset.dataSetSource, Option(Schema(cleanColumns, cleanColumns.length)))
  }
}
