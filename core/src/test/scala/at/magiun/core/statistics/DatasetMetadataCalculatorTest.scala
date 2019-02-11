package at.magiun.core.statistics

import at.magiun.core.TestData._
import at.magiun.core.connector.CsvConnector
import at.magiun.core.model.math.MagiunMatrix
import at.magiun.core.{MainModule, UnitTest}
import org.scalatest.PrivateMethodTester

class DatasetMetadataCalculatorTest extends UnitTest with PrivateMethodTester {

  private val mainModule = new MainModule {}
  private  val sparkSession = mainModule.spark
  private val connector = new CsvConnector(sparkSession)
  private val metadataCalculator = mainModule.datasetMetadataCalculator

  import sparkSession.implicits._

  it should "calculate the correlation matrix for a small dataset" in {
    import sparkSession.implicits._

    val dataset = Seq(
        (1, 2, "a"),
        (3, 4, "b"),
        (5, 6, "c"),
        (7, 8, "d"),
        (9, 10, "e")
      ).toDF("number1", "number2", "character")


    val computeCorrelationMatrix = PrivateMethod[DatasetMetadataCalculator]('computeCorrelationMatrix)
    val matrix = (metadataCalculator invokePrivate computeCorrelationMatrix(dataset, "pearson")).asInstanceOf[MagiunMatrix]

    matrix.numCols shouldBe 2
    matrix.numRows shouldBe 2
    assertCorrelationMatrixDiagonal(matrix)
  }

  it should "calculate the correlation matrix for the housing data set" in {
    val dataset = connector.getDataset(housingDatasetSource)
    val computeCorrelationMatrix = PrivateMethod[DatasetMetadataCalculator]('computeCorrelationMatrix)
    val matrix = (metadataCalculator invokePrivate computeCorrelationMatrix(dataset, "pearson")).asInstanceOf[MagiunMatrix]

    matrix.numCols shouldBe 10
    matrix.numRows shouldBe 10
    assertCorrelationMatrixDiagonal(matrix)
  }

  private def assertCorrelationMatrixDiagonal(matrix: MagiunMatrix): Unit = {
    Range.apply(0, matrix.numRows - 1).foreach(i => {
      matrix(i, i) shouldBe 1
    })
  }

}
