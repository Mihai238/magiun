package at.magiun.core.statistics.trainer

import at.magiun.core.MagiunContext
import at.magiun.core.model.algorithm.{Algorithm, BinaryLogisticRegressionAlgorithm, DecisionTreeClassificationAlgorithm, DecisionTreeRegressionAlgorithm, GeneralizedLinearRegressionAlgorithm, GradientBoostTreeRegressionAlgorithm, LinearRegressionAlgorithm, MultinomialLogisticRegressionAlgorithm, RandomForestRegressionAlgorithm}
import at.magiun.core.model.rest.response.TrainAlgorithmResponse
import at.magiun.core.statistics.trainer.classification.{DecisionTreeClassificationTrainer, LogisticRegressionAlgorithmTrainer}
import at.magiun.core.statistics.trainer.regression.{DecisionTreeRegressionTrainer, GeneralizedLinearRegressionAlgorithmTrainer, GradientBoostTreeRegressionTrainer, LinearRegressionAlgorithmTrainer, RandomForestRegressionTrainer}
import org.apache.spark.sql.DataFrame

class AlgorithmTrainer(magiunContext: MagiunContext) {

  private val SAMPLE_SIZE = 1000

  def train(algorithm: Algorithm[_ <: Any], dataFrame: DataFrame, responseVariableName: String, explanatoryVariablesNames: Array[String]): TrainAlgorithmResponse = {
    try {
      algorithm match {
        case _: LinearRegressionAlgorithm =>
          LinearRegressionAlgorithmTrainer.train(algorithm.asInstanceOf[LinearRegressionAlgorithm], dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext, SAMPLE_SIZE)
        case _: GeneralizedLinearRegressionAlgorithm =>
          GeneralizedLinearRegressionAlgorithmTrainer.train(algorithm.asInstanceOf[GeneralizedLinearRegressionAlgorithm], dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext, SAMPLE_SIZE)
        case _: DecisionTreeRegressionAlgorithm =>
          DecisionTreeRegressionTrainer.train(algorithm.asInstanceOf[DecisionTreeRegressionAlgorithm], dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext, SAMPLE_SIZE)
        case _: RandomForestRegressionAlgorithm =>
          RandomForestRegressionTrainer.train(algorithm.asInstanceOf[RandomForestRegressionAlgorithm], dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext, SAMPLE_SIZE)
        case _: GradientBoostTreeRegressionAlgorithm =>
          GradientBoostTreeRegressionTrainer.train(algorithm.asInstanceOf[GradientBoostTreeRegressionAlgorithm], dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext, SAMPLE_SIZE)
        case _: BinaryLogisticRegressionAlgorithm =>
          LogisticRegressionAlgorithmTrainer.train(algorithm.asInstanceOf[BinaryLogisticRegressionAlgorithm], dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext, SAMPLE_SIZE)
        case _: MultinomialLogisticRegressionAlgorithm =>
          LogisticRegressionAlgorithmTrainer.train(algorithm.asInstanceOf[MultinomialLogisticRegressionAlgorithm], dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext, SAMPLE_SIZE)
        case _: DecisionTreeClassificationAlgorithm =>
          DecisionTreeClassificationTrainer.train(algorithm.asInstanceOf[DecisionTreeClassificationAlgorithm], dataFrame, responseVariableName, explanatoryVariablesNames, magiunContext, SAMPLE_SIZE)
        case _ => new TrainAlgorithmResponse(s"Algorithm ${algorithm.name} is not implemented yet!")
      }
    } catch {
      case e: Exception =>
        e.printStackTrace()
        new TrainAlgorithmResponse(s"An error occurred while trying to train the model! ${e.getMessage}")
    }
  }
}