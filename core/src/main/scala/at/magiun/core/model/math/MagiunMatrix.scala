package at.magiun.core.model.math

import org.apache.spark.mllib.linalg.Matrix

sealed case class MagiunMatrix(matrix: Matrix, columnNames: Seq[String], rowNames: Seq[String]) {

  if (columnNames.nonEmpty) {
    require(columnNames.length == matrix.numCols, s"The number of column names ${columnNames.length} does not match the number of columns ${matrix.numCols}")
  }

  if (rowNames.nonEmpty) {
    require(rowNames.length == matrix.numCols, s"The number of row names ${rowNames.length} does not match the number of rows ${matrix.numRows}")
  }

  def numCols: Int = matrix.numCols
  def numRows: Int = matrix.numRows
  def apply(i: Int, j: Int): Double = matrix(1,1)
}
