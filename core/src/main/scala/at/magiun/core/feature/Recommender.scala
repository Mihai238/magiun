package at.magiun.core.feature

import at.magiun.core.config.FeatureEngOntology
import at.magiun.core.config.OntologyConfig.NS
import com.softwaremill.tagging.@@
import com.typesafe.scalalogging.LazyLogging
import org.apache.jena.ontology.{Individual, OntModel, OntResource}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import scala.collection.JavaConversions._

/**
  *
  */
class Recommender(sparkSession: SparkSession,
                  model: OntModel @@ FeatureEngOntology,
                  restrictionBuilder: RestrictionBuilder,
                  columnMetaDataComputer: ColumnMetaDataComputer) extends LazyLogging {

  private lazy val restrictions: Map[String, Restriction] = restrictionBuilder.build(model)

  private lazy val cardinalityProperty = model.getProperty(NS + "uniqueCount")
  private lazy val missingValuesProperty = model.getProperty(NS + "missingValues")
  private lazy val countProperty = model.getProperty(NS + "count")
  private lazy val meanProperty = model.getProperty(NS + "mean")
  private lazy val stddevProperty = model.getProperty(NS + "stddev")
  private lazy val minProperty = model.getProperty(NS + "min")
  private lazy val maxProperty = model.getProperty(NS + "max")
  private lazy val medianProperty = model.getProperty(NS + "median")

  private lazy val hasValueProperty = model.getProperty(NS + "hasValue")
  private lazy val hasDistributionProperty = model.getProperty(NS + "hasDistribution")

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
    columnsMetadata.map { colMeta =>
      val (indv, tmpRes) = createIndividual(colMeta)

      val colTypes = model.listStatements(indv.asResource(), null, null).toList.toList
        .filter(model.contains)
        .filter(_.getPredicate.getLocalName == "type")
        .filter(_.getObject.asResource().getNameSpace == NS)
        .map(_.getObject.asResource().getLocalName)

      tmpRes.foreach(e => model.removeAll(e, null, null))
      model.removeAll(indv, null, null)

      colTypes
    }
  }

  private def createIndividual(colMeta: ColumnMetaData): (Individual, Set[OntResource]) = {
    val indv = model.createIndividual(model.getOntClass(NS + "Column"))

    indv.addProperty(cardinalityProperty, model.createTypedLiteral(colMeta.uniqueValues.asInstanceOf[Any]))
    indv.addProperty(missingValuesProperty, model.createTypedLiteral(colMeta.missingValues.asInstanceOf[Integer]))

    indv.addProperty(countProperty, model.createTypedLiteral(colMeta.stats.count.asInstanceOf[Any]))
    colMeta.stats.mean.foreach(x => indv.addProperty(meanProperty, model.createTypedLiteral(x.asInstanceOf[Any])))
    colMeta.stats.stddev.foreach(x => indv.addProperty(stddevProperty, model.createTypedLiteral(x.asInstanceOf[Any])))
    colMeta.stats.min.foreach(x => indv.addProperty(minProperty, model.createTypedLiteral(x.asInstanceOf[Any])))
    colMeta.stats.max.foreach(x => indv.addProperty(maxProperty, model.createTypedLiteral(x.asInstanceOf[Any])))
    colMeta.stats.median.foreach(x => indv.addProperty(medianProperty, model.createTypedLiteral(x.asInstanceOf[Any])))

    val tmpDistrIndvs = colMeta.distributions.map(distrType => {
      val tmpIndv = model.createIndividual(model.getOntClass(NS + distrType.ontClassName))
      indv.addProperty(hasDistributionProperty, tmpIndv)
      tmpIndv
    })

    var list = model.createList()
    val tmpValueRestr1 = colMeta.intersectedValueTypes.map(valueType => {
      val tmpRestr = model.createAllValuesFromRestriction(null, hasValueProperty, model.getOntClass(NS + valueType))
      list = list.`with`(tmpRestr)
      tmpRestr
    })

    val tmpValueRestr2 = colMeta.unionValueTypes.map(valueType => {
      val tmpRestr = model.createSomeValuesFromRestriction(null, hasValueProperty, model.getOntClass(NS + valueType))
      list = list.`with`(tmpRestr)
      tmpRestr
    })

    val intersectionClass = model.createIntersectionClass(null, list)
    indv.addOntClass(intersectionClass)

    (indv, tmpDistrIndvs ++ tmpValueRestr1 ++ tmpValueRestr2 + intersectionClass)
  }
}

/**
  *
  */
case class Recommendations(map: Map[Int, Recommendation])

case class Recommendation(colTypes: List[String], operations: List[String])