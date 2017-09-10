package at.magiun.core.model

import org.apache.spark.sql.{Dataset, Row}

abstract class StageOutput
case class DatasetOutput(dataSet: Dataset[Row]) extends StageOutput
