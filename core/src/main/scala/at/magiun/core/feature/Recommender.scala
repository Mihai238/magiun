package at.magiun.core.feature

import com.typesafe.scalalogging.LazyLogging
import org.apache.jena.ontology.OntModel
import org.apache.spark.sql.{Dataset, Row, SparkSession}

/**
  *
  */
class Recommender(sparkSession: SparkSession,
                  model: OntModel,
                  restrictionBuilder: RestrictionBuilder,
                  columnMetaDataComputer: ColumnMetaDataComputer,
                  columnTypeRecommender: ColumnTypeRecommender,
                  operationRecommender: OperationRecommender) extends LazyLogging {

  def recommendFeatureOperation(ds: Dataset[Row]): Recommendations = {
    val restrictions = restrictionBuilder.build(model)
    val columnsMetaData = columnMetaDataComputer.compute(ds, restrictions)
    val columnTypes = columnTypeRecommender.recommend(columnsMetaData)

    val recs = columnTypes.map(colType => (colType._1, Recommendation(colType._2, List())))
    Recommendations(recs)
  }
}

/**
  *
  */
case class Recommendations(map: Map[Int, Recommendation])

case class Recommendation(colTypes: List[String], operations: List[String])