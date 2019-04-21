package at.magiun.core.util

import org.apache.spark.sql.{Dataset, Row}

object DatasetUtil {

  def cleanDatasetFromUnnecessaryVariables(dataset: Dataset[Row], responseVariable: Int, explanatoryVariables: Seq[Int]): Dataset[Row] = {
    val columns = dataset.columns
    val columnsToRemove: Seq[String] = columns.indices
      .filter(i => !(explanatoryVariables.contains(i) || i == responseVariable))
      .map(i => columns(i))

    columnsToRemove.foldLeft(dataset)((df, col) => df.drop(col))
  }

}
