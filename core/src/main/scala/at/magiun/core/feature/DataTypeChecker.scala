package at.magiun.core.feature
import org.apache.jena.rdf.model.{RDFNode, Statement}
import org.apache.spark.sql.{Dataset, Row, SparkSession}

class DataTypeChecker extends Checker {

  val TypeIntClass = "integer"
  val TypeDoubleClass = "double"
  val TypeStringClass = "string"

  val requiredToAllowedType = Map(
    "integer" -> Set("integer"),
    "double" -> Set("double"),
    "string" -> Set("string")
  )

  override def check(sparkSession: SparkSession, ds: Dataset[Row], colIndex: Int, restriction: Statement): Boolean = {
    val requiredType = restriction.getObject.asResource().getLocalName
    val sparkColumnType = ds.schema(colIndex).dataType.typeName
    requiredToAllowedType(requiredType).contains(sparkColumnType)
  }

}
