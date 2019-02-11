package at.magiun.core.statistics

import at.magiun.core.TestData._
import at.magiun.core.connector.CsvConnector
import at.magiun.core.model.math.MagiunMatrix
import at.magiun.core.{MainModule, UnitTest}
import org.apache.spark.sql.{Dataset, Row}
import org.scalatest.PrivateMethodTester

class DatasetMetadataCalculatorTest extends UnitTest with PrivateMethodTester {

  private val mainModule = new MainModule {}
  private  val sparkSession = mainModule.spark
  private val connector = new CsvConnector(sparkSession)
  private val metadataCalculator = mainModule.datasetMetadataCalculator

  it should "calculate the correlation matrix for a small dataset" in {
    // given
    val dataset = givenUncorrelatedDataset

    // when
    val computeCorrelationMatrix = PrivateMethod[DatasetMetadataCalculator]('computeCorrelationMatrix)
    val matrix = (metadataCalculator invokePrivate computeCorrelationMatrix(dataset, "pearson")).asInstanceOf[MagiunMatrix]

    // then
    matrix.numCols shouldBe 2
    matrix.numRows shouldBe 2
    assertCorrelationMatrixDiagonal(matrix)
  }

  it should "calculate the correlation matrix for the housing data set" in {
    // given
    val dataset = connector.getDataset(housingDatasetSource)

    // when
    val computeCorrelationMatrix = PrivateMethod[DatasetMetadataCalculator]('computeCorrelationMatrix)
    val matrix = (metadataCalculator invokePrivate computeCorrelationMatrix(dataset, "pearson")).asInstanceOf[MagiunMatrix]

    // then
    matrix.numCols shouldBe 10
    matrix.numRows shouldBe 10
    assertCorrelationMatrixDiagonal(matrix)
  }

  it should "calculate the multicollinearity and it should be false" in {
    // given
    val dataset = givenUncorrelatedDataset

    // when
    val computeMulticollinearity = PrivateMethod[DatasetMetadataCalculator]('computeMulticollinearity)
    val multicollinearity = (metadataCalculator invokePrivate computeMulticollinearity(dataset)).asInstanceOf[Boolean]

    // then
    multicollinearity shouldBe false
  }

  it should "calculate the multicollinearity and it should be true" in {
    // given
    val dataset = givenCorrelatedDataset

    // when
    val computeMulticollinearity = PrivateMethod[DatasetMetadataCalculator]('computeMulticollinearity)
    val multicollinearity = (metadataCalculator invokePrivate computeMulticollinearity(dataset)).asInstanceOf[Boolean]

    // then
    multicollinearity shouldBe true
  }

  private def assertCorrelationMatrixDiagonal(matrix: MagiunMatrix): Unit = {
    (0 until matrix.numRows).foreach(i => {
      matrix(i, i) shouldBe 1
    })
  }

  private def givenUncorrelatedDataset: Dataset[Row] = {
    import sparkSession.implicits._

    Seq(
      (1, 21, "a"),
      (3, 453, "b"),
      (5, 60, "c"),
      (7, -8, "d"),
      (9, 4, "e")
    ).toDF("number1", "number2", "character")
  }

  private def givenCorrelatedDataset: Dataset[Row] = {
    import sparkSession.implicits._

    Seq(
      (1, 11, 21, 31, 41, "a"),
      (3, 13, 23, 33, 43, "b"),
      (5, 15, 25, 35, 45, "c"),
      (7, 17, 27, 37, 47, "d"),
      (9, 19, 29, 39, 49, "e")
    ).toDF("number1", "number2", "number3", "number4", "number5", "character")
  }
}
