package at.magiun.core.statistics.trainer.classification

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.DataFrame

trait ClassificationAlgorithmTrainer {
  protected def transformDF(
                             dataFrame: DataFrame,
                             explanatoryVariablesNames: Array[String]
                           ): DataFrame = {
    val vectorAssembler = new VectorAssembler()
      .setInputCols(explanatoryVariablesNames)
      .setOutputCol("features")

    vectorAssembler.transform(dataFrame)
  }

}
