package at.magiun.core.statistics.trainer

import at.magiun.core.MagiunContext
import at.magiun.core.model.algorithm.{Algorithm, LinearRegressionAlgorithm}
import at.magiun.core.model.response.TrainAlgorithmResponse
import org.apache.spark.sql.DataFrame

class AlgorithmTrainer(magiunContext: MagiunContext) {

  def train(algorithm: Algorithm[_ <: Any], dataFrame: DataFrame, responseVariableName: String, explanatoryVariablesNames: Array[String]): TrainAlgorithmResponse = {

    algorithm match {
      case _: LinearRegressionAlgorithm =>
        LinearRegressionAlgorithmTrainer.train(algorithm.asInstanceOf[LinearRegressionAlgorithm], dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext)
      case _ => new TrainAlgorithmResponse(s"Algorithm ${algorithm.name} is not implemented yet!")
    }
  }
}
