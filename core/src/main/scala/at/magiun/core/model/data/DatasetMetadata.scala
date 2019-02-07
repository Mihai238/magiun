package at.magiun.core.model.data

import at.magiun.core.model.algorithm.AlgorithmGoal


// todo to be extended
case class DatasetMetadata(goal: AlgorithmGoal,
                            responseVariableType: VariableType,
                           responseVariableDistribution: Distribution,
                           normalDistributionPercentage: Double,
                           continuousVariableTypePercentage: Double,
                           observationVariableRatio: Double
                          ) {

}
