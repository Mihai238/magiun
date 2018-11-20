package at.magiun.core.feature

import at.magiun.core.config.OntologyConfig.NS
import com.typesafe.scalalogging.LazyLogging
import org.apache.jena.ontology.OntModel
import org.apache.spark.sql.{Dataset, Row, SparkSession}
import scala.collection.JavaConversions._

/**
  *
  */
class Recommender(sparkSession: SparkSession,
                  model: OntModel,
                  restrictionBuilder: RestrictionBuilder,
                  columnMetaDataComputer: ColumnMetaDataComputer) extends LazyLogging {


  private lazy val restrictions: Map[String, Restriction] = restrictionBuilder.build(model)

  def recommend(ds: Dataset[Row]): Recommendations = {
    val columnsMetaData = columnMetaDataComputer.compute(ds, restrictions)
    logger.info("Predicting columns type.")
    val columnTypes = recommendIntern(columnsMetaData)

    val recs = columnTypes.map(colType => {
      val (types, operations) = colType
        .filter(e => e != "Column" && e != "OperationSuitableColumn")
        .partition(e => !isOperationSuitable(e))

      Recommendation(types, operations)
    })
      .zipWithIndex.map { case (x, y) => (y, x) }
      .toMap

    logger.info("Recommender done.")
    Recommendations(recs)
  }

  private def isOperationSuitable(colType: String): Boolean = {
    colType.endsWith("SuitableColumn")
  }

  def recommendIntern(columnsMetadata: Seq[ColumnMetaData]): Seq[List[String]] = {
    val cardinalityProperty = model.getProperty(NS + "cardinality")
    val missingValuesProperty = model.getProperty(NS + "missingValues")
    val uniformDistributionProperty = model.getProperty(NS + "uniformDistribution")
    val hasValueProperty = model.getProperty(NS + "hasValue")

    columnsMetadata.map { colMeta =>
      val valueTypes = colMeta.valueTypes
      val indv = model.createIndividual(model.getOntClass(NS + "Column"))
      indv.addProperty(cardinalityProperty, model.createTypedLiteral(colMeta.uniqueValues.asInstanceOf[Any]))
      indv.addProperty(missingValuesProperty, model.createTypedLiteral(colMeta.missingValues.asInstanceOf[Integer]))
      indv.addProperty(uniformDistributionProperty, model.createTypedLiteral(colMeta.distributions.uniform.asInstanceOf[Any]))
      val tmpIndvs = valueTypes.map(valueType => {
        val tmpIndv = model.createIndividual(model.getOntClass(NS + valueType))
        indv.addProperty(hasValueProperty, tmpIndv)
        tmpIndv
      })

      val colTypes = model.listStatements(indv.asResource(), null, null).toList.toList
        .filter(model.contains)
        .filter(_.getPredicate.getLocalName == "type")
        .filter(_.getObject.asResource().getNameSpace == NS)
        .map(_.getObject.asResource().getLocalName)

      tmpIndvs.foreach(e => model.removeAll(e, null, null))
      model.removeAll(indv, null, null)

      colTypes
    }
  }
}

/**
  *
  */
case class Recommendations(map: Map[Int, Recommendation])

case class Recommendation(colTypes: List[String], operations: List[String])