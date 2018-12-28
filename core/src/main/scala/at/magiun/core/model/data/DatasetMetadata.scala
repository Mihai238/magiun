package at.magiun.core.model.data


// todo to be extended
case class DatasetMetadata(responseVariableType: VariableType,
                           responseVariableDistribution: Distribution,
                           normalDistributionPercentage: Double,
                           continuousVariableTypePercentage: Double,
                           observationVariableRatio: Double,
                           variablesCount: Long,
                           observationsCount: Long
                          ) {
}
