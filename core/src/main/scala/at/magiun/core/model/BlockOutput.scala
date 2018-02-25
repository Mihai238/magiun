package at.magiun.core.model

import org.apache.spark.sql.{Dataset, Row}

abstract class BlockOutput
case class DatasetOutput(dataSet: Dataset[Row]) extends BlockOutput
case class MultiOutput(list: Seq[BlockOutput]) extends BlockOutput