package at.magiun.core.util

import at.magiun.core.model.DataRow
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.{Dataset, Row}

import scala.Option.empty
import scala.util.Random

object DatasetUtil {

  def cleanDatasetFromUnnecessaryVariables(dataset: Dataset[Row], responseVariable: Int, explanatoryVariables: Seq[Int]): Dataset[Row] = {
    val columns = dataset.columns
    val columnsToRemove: Seq[String] = columns.indices
      .filter(i => !(explanatoryVariables.contains(i) || i == responseVariable))
      .map(i => columns(i))

    columnsToRemove.foldLeft(dataset)((df, col) => df.drop(col))
  }

   def mapToRowValues(dfRows: Array[Row], schema: StructType, columns: Option[Seq[String]] = empty): Array[DataRow] = {
    dfRows
      .zipWithIndex
      .map { case (sparkRow, rowInd) =>

        val values = if (columns.isDefined) {
          columns.map(_.flatMap {
            col =>
              val colIndex = schema.zipWithIndex.find(e => e._1.name == col).get._2
              Option(sparkRow.get(colIndex)).map(_.toString)
          }).get
        } else {
          schema.zipWithIndex.flatMap {
            case (col, colInd) =>
              Option(sparkRow.get(colInd)).orElse(Option("")).map(_.toString)
          }
        }

        DataRow(rowInd, values)
      }
  }

  def getRandomIndices(size: Int, dataCount: Int): Seq[Int] = {
    val r = new Random()
    (0 until size).map(_ => r.nextInt(dataCount))
  }

  def getRowsByIndices(dataset: Dataset[Row], indices: Seq[Int]): Array[Row] = {
    dataset.rdd.zipWithIndex().filter{case (_, v) => indices.contains(v)}.map{case (k, _) => k}.collect()
  }

}
