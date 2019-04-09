package at.magiun.core.statistics

import at.magiun.core.config.FeatureEngOntology
import at.magiun.core.feature.RestrictionBuilder
import at.magiun.core.model.{Column, ColumnType, MagiunDataSet}
import at.magiun.core.model.algorithm.AlgorithmGoal
import at.magiun.core.model.data.{DatasetMetadata, Distribution, VariableType}
import at.magiun.core.model.math.MagiunMatrix
import at.magiun.core.model.request.RecommenderRequest
import com.softwaremill.tagging.@@
import com.typesafe.scalalogging.LazyLogging
import org.apache.jena.ontology.OntModel
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.sql._

import scala.collection.mutable

class DatasetMetadataCalculator(sparkSession: SparkSession,
                                model: OntModel @@ FeatureEngOntology,
                                restrictionBuilder: RestrictionBuilder
                               ) extends LazyLogging {

  private val correlationThreshold = 0.9

  // TODO: implement me
  def compute(request: RecommenderRequest, dataset: Dataset[Row], magiunDataset: MagiunDataSet): DatasetMetadata = {
    val multicollinearity = computeMulticollinearity(dataset)
    val observationVariableRatio = dataset.count() / dataset.columns.length
    val explanatoryVariablesCount = request.explanatoryVariables.size
    val distributionsMap = request.explanatoryVariablesDistributions.groupBy(identity).mapValues(_.size)
    val responseVariableDistribution = request.responseVariableDistribution
    val columns = magiunDataset.schema.get.columns
    val responseVariableColumn = columns.find(c => c.index == request.responseVariable).get
    val variableTypesMap = computeVariableTypesMap(request.explanatoryVariables, request.explanatoryVariablesDistributions, columns)

    DatasetMetadata(
      AlgorithmGoal.getFromString(request.goal),
      computeVariableType(responseVariableColumn, responseVariableDistribution),
      responseVariableDistribution,
      computeElementOccurrencePercentage(distributionsMap, Distribution.Normal, explanatoryVariablesCount),
      computeElementOccurrencePercentage(distributionsMap, Distribution.Bernoulli, explanatoryVariablesCount),
      computeElementOccurrencePercentage(distributionsMap, Distribution.Multinomial, explanatoryVariablesCount),
      computeElementOccurrencePercentage(variableTypesMap, VariableType.Continuous, explanatoryVariablesCount),
      computeElementOccurrencePercentage(variableTypesMap, VariableType.Binary, explanatoryVariablesCount),
      computeElementOccurrencePercentage(variableTypesMap, VariableType.Discrete, explanatoryVariablesCount),
      observationVariableRatio,
      multicollinearity
    )
  }

  /**
    * Following links are directing to topics where it is discussed the correlation threshold value which indicates multicollinearity
    *
    * @see https://www.researchgate.net/post/Whats_the_difference_between_correlation_and_VIF
    * @see https://stats.stackexchange.com/questions/100175/when-can-we-speak-of-collinearity
    */
  private def computeMulticollinearity(dataset: Dataset[Row]): Boolean = {
    val correlationMatrix = computeCorrelationMatrix(dataset)
    val queue = mutable.Queue[(String, String, Double)]()

    (1 until correlationMatrix.numCols).foreach(i => {
      (0 until i).foreach(j => {
        if (correlationMatrix(i, j) >= correlationThreshold) {
          queue += Tuple3(correlationMatrix.columnNames(i), correlationMatrix.rowNames(j), correlationMatrix(i, j))
        }
      })
    })

    if (queue.isEmpty) {
      false
    } else {
      logger.warn(s"Following variables are high correlated ${queue.toString()}")
      true
    }
  }

  private def computeCorrelationMatrix(dataset: Dataset[Row], method: String = "pearson"): MagiunMatrix = {
    val columnsToRemove = dataset.dtypes.filterNot(dtype => dtype._2.contains("Integer") | dtype._2.contains("Double")).map(_._1)

    val featureDataset = columnsToRemove
      .foldLeft(dataset)((df, col) => df.drop(col))
      .na.fill(0)


    val columnNames = featureDataset.columns

    val featureRDD = featureDataset.rdd
      .map { row => Vectors.dense(row.toSeq.map(_.toString.toDouble).toArray) }

    MagiunMatrix(Statistics.corr(featureRDD, method), columnNames, columnNames)
  }

  private def computeVariableType(column: Column, distribution: Distribution): VariableType = {
    if (column.`type` == ColumnType.Double || column.`type` == ColumnType.Int) {
      if (Distribution.isItDiscrete(distribution)) {
        if (distribution == Distribution.Bernoulli) {
          VariableType.Binary
        } else {
          VariableType.Discrete
        }
      } else {
        VariableType.Continuous
      }
    } else {
      VariableType.Text
    }
  }

  private def computeVariableTypesMap(explanatoryVariables: Seq[Int], distributions: Seq[Distribution], columns: List[Column]): Map[VariableType, Int] = {
    explanatoryVariables.indices.map { i =>
      val variableIndex = explanatoryVariables(i)
      val column = columns.find(c => c.index == variableIndex).get
      computeVariableType(column, distributions(i))
    }.groupBy(identity)
      .mapValues(_.size)
  }

  private def computeElementOccurrencePercentage[T](map: Map[T, Int], element: T, totalCount: Int): Double = {
    map.get(element).map { x =>
      if (x == 0) {
        return 0.0
      } else {
        return 1.0 * x / totalCount
      }
    }.getOrElse(0.0)
  }
}
