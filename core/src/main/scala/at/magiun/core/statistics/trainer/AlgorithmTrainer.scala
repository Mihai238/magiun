package at.magiun.core.statistics.trainer

import at.magiun.core.MagiunContext
import at.magiun.core.model.algorithm.{Algorithm, GeneralizedLinearRegressionAlgorithm, LinearRegressionAlgorithm}
import at.magiun.core.model.rest.response.TrainAlgorithmResponse
import org.apache.spark.sql.DataFrame

class AlgorithmTrainer(magiunContext: MagiunContext) {

  private val SAMPLE_SIZE = 1000

  def train(algorithm: Algorithm[_ <: Any], dataFrame: DataFrame, responseVariableName: String, explanatoryVariablesNames: Array[String]): TrainAlgorithmResponse = {

    algorithm match {
      case _: LinearRegressionAlgorithm =>
        LinearRegressionAlgorithmTrainer.train(algorithm.asInstanceOf[LinearRegressionAlgorithm], dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext, SAMPLE_SIZE)
      case _: GeneralizedLinearRegressionAlgorithm =>
        GeneralizedLinearRegressionAlgorithmTrainer.train(algorithm.asInstanceOf[GeneralizedLinearRegressionAlgorithm], dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext, SAMPLE_SIZE)

      case _ => new TrainAlgorithmResponse(s"Algorithm ${algorithm.name} is not implemented yet!")
    }
  }
}