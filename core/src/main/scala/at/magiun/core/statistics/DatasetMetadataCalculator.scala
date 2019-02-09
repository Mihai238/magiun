package at.magiun.core.statistics

import at.magiun.core.config.FeatureEngOntology
import at.magiun.core.feature.{Restriction, RestrictionBuilder}
import at.magiun.core.model.MagiunDataSet
import at.magiun.core.model.data.DatasetMetadata
import at.magiun.core.model.request.RecommenderRequest
import com.softwaremill.tagging.@@
import com.typesafe.scalalogging.LazyLogging
import org.apache.jena.ontology.OntModel
import org.apache.spark.mllib.linalg.{Matrix, Vectors}
import org.apache.spark.mllib.stat.Statistics
import org.apache.spark.sql._

class DatasetMetadataCalculator(sparkSession: SparkSession,
                                columnMetaDataCalculator: ColumnMetadataCalculator,
                                model: OntModel @@ FeatureEngOntology,
                                restrictionBuilder: RestrictionBuilder
                               ) extends LazyLogging {

  private lazy val restrictions: Map[String, Restriction] = restrictionBuilder.build(model)

  // TODO: implement me
  def compute(request: RecommenderRequest, dataset: Dataset[Row], magiunDataset: MagiunDataSet): DatasetMetadata = {
    val columnMetadata = columnMetaDataCalculator.compute(dataset, restrictions)
    val multicollinearity = computeMulticollinearity(dataset)
    null
  }

  // TODO: implement me
  private def computeMulticollinearity(dataset: Dataset[Row]): Boolean = {
    val correlationMatrix = computeMulticollinearity(dataset)

    false
  }

  private def computeCorrelationMatrix(dataset: Dataset[Row], method: String = "pearson"): Matrix = {
    val columnsToRemove = dataset.dtypes.filterNot(dtype => dtype._2.contains("Integer") | dtype._2.contains("Double")).map(_._1)

    val featureRDD = columnsToRemove
      .foldLeft(dataset)((df, col) => df.drop(col))
      .na.fill(0)
      .rdd
      .map{row => Vectors.dense(row.toSeq.map(_.toString.toDouble).toArray)}

    Statistics.corr(featureRDD, method)
  }
}
