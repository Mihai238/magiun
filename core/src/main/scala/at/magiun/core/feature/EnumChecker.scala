package at.magiun.core.feature

import org.apache.jena.rdf.model.{RDFList, Statement}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

import scala.collection.JavaConversions._

class EnumChecker extends Checker {

  override def check(sparkSession: SparkSession, ds: Dataset[Row], colIndex: Int, enumRestriction: Statement): Boolean = {

    val list = enumRestriction.getObject.asResource().as(classOf[RDFList]).iterator().toList.toList
    val valueSet: Set[String] = list.map(_.asLiteral().toString).toSet

    new DatasetOperator(sparkSession, ds)
      .allMatch(colIndex, valueSet.contains)
  }

}
