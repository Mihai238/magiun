package at.magiun.core.model

import org.apache.spark.sql.{Dataset, Row}

trait StageOutput
case class DatasetOutput(dataSet: Dataset[Row]) extends StageOutput
case class MultiOutput(list: Seq[StageOutput]) extends StageOutput
object EmptyOutput extends StageOutput