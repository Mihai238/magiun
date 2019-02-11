package at.magiun.core.statistics

import at.magiun.core.config.FeatureEngOntology
import at.magiun.core.feature.{Restriction, RestrictionBuilder}
import at.magiun.core.model.MagiunDataSet
import at.magiun.core.model.data.DatasetMetadata
import at.magiun.core.model.math.MagiunMatrix
import at.magiun.core.model.request.RecommenderRequest
import com.softwaremill.tagging.@@
import com.typesafe.scalalogging.LazyLogging
import org.apache.jena.ontology.OntModel
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.sql._

class DatasetMetadataCalculator(sparkSession: SparkSession,
                                columnMetaDataCalculator: ColumnMetadataCalculator,
                                model: OntModel @@ FeatureEngOntology,
                                restrictionBuilder: RestrictionBuilder
                               ) extends LazyLogging {

  private val correlationThreshold = 0.9
  private lazy val restrictions: Map[String, Restriction] = restrictionBuilder.build(model)

  // TODO: implement me
  def compute(request: RecommenderRequest, dataset: Dataset[Row], magiunDataset: MagiunDataSet): DatasetMetadata = {
    val columnMetadata = columnMetaDataCalculator.compute(dataset, restrictions)
    val multicollinearity = computeMulticollinearity(dataset)
    null
  }

  /**
    * Following links are directing to topics where it is discussed the correlation threshold value which indicates multicollinearity
    *
    * https://www.researchgate.net/post/Whats_the_difference_between_correlation_and_VIF
    * https://stats.stackexchange.com/questions/100175/when-can-we-speak-of-collinearity
    */
  private def computeMulticollinearity(dataset: Dataset[Row]): Boolean = {
    val correlationMatrix = computeCorrelationMatrix(dataset)

    false
  }

  private def computeCorrelationMatrix(dataset: Dataset[Row], method: String = "pearson"): MagiunMatrix = {
    val columnsToRemove = dataset.dtypes.filterNot(dtype => dtype._2.contains("Integer") | dtype._2.contains("Double")).map(_._1)

    val featureDataset = columnsToRemove
      .foldLeft(dataset)((df, col) => df.drop(col))
      .na.fill(0)


    val columnNames = featureDataset.columns

    val featureRDD = featureDataset.rdd
      .map{row => Vectors.dense(row.toSeq.map(_.toString.toDouble).toArray)}

    MagiunMatrix(Statistics.corr(featureRDD, method), columnNames, columnNames)
  }
}
