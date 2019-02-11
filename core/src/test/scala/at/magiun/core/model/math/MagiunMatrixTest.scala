package at.magiun.core.model.math

import at.magiun.core.UnitTest
import org.apache.spark.mllib.linalg.{DenseMatrix, Matrix}

class MagiunMatrixTest extends UnitTest {

  private val matrix: Matrix = new DenseMatrix(3, 2, Array[Double](1, 2, 3, 4, 5, 6))

  it should "throw an exception when attempting to create a matrix where nCols > columnNames.length" in {
    assertThrows[IllegalArgumentException] {
      MagiunMatrix(matrix, Seq("col1"))
    }
  }

  it should "throw an exception when attempting to create a matrix where nCols < columnNames.length" in {
    assertThrows[IllegalArgumentException] {
      MagiunMatrix(matrix, Seq("col1", "col2", "col3"))
    }
  }

  it should "throw an exception when attempting to create a matrix where nRows > rowNames.length" in {
    assertThrows[IllegalArgumentException] {
      MagiunMatrix(matrix, rowNames = Seq("row1"))
    }
  }

  it should "throw an exception when attempting to create a matrix where nRows < rowNames.length" in {
    assertThrows[IllegalArgumentException] {
      MagiunMatrix(matrix, rowNames = Seq("row1", "row2", "row3", "row4"))
    }
  }

  it should "create magiun matrix" in {
    val magiunMatrix = MagiunMatrix(matrix, Seq("col1", "col2"), Seq("row1", "row2", "row3"))
    magiunMatrix(0,0) shouldBe 1
    magiunMatrix(1,1) shouldBe 5
  }

}
