package at.magiun.core.model.data


// todo to be extended
case class DatasetMetadata(variableTypes: Seq[VariableType],
                           variableDistributions: Seq[Distribution],
                           responseVariableIndex: Int,
                           explanatoryVariablesToIgnoreIndex: Seq[Int],
                           variablesCount: Int,
                           observationsCount: Int) {
}
