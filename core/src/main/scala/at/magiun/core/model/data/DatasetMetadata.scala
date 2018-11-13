package at.magiun.core.model.data


// todo to be extended
case class DatasetMetadata(variableTypes: Seq[VariableType],
                           variableDistributions: Seq[Distribution],
                           responseVariableIndex: Int,
                           variablesToIgnoreIndex: Seq[Int],
                           variablesCount: Long,
                           observationsCount: Long) {
}
