package at.magiun.core.service

import at.magiun.core.connector.{Connector, CsvConnector, MemoryConnector, MongoDbConnector}
import at.magiun.core.model.SourceType
import at.magiun.core.model.SourceType.{FileCsv, Memory, Mongo}
import org.apache.spark.sql.SparkSession

class ConnectorFactory(spark: SparkSession, executionContext: ExecutionContext) {

  def getConnector(sourceType: SourceType): Connector = sourceType match {
    case FileCsv => new CsvConnector(spark)
    case Mongo => new MongoDbConnector(spark)
    case Memory => new MemoryConnector(executionContext.getExecutionsOutput)
  }

}
