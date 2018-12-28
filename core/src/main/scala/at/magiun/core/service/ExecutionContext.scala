package at.magiun.core.service

import java.util
import java.util.concurrent.ConcurrentHashMap

import at.magiun.core.model.StageOutput

import scala.collection.JavaConversions._

class ExecutionContext {

  private val executionsMap: util.Map[String, StageOutput] = new ConcurrentHashMap[String, StageOutput]

  def registerExecution(execId: String, output: StageOutput): Unit = {
    executionsMap.put(execId, output)
  }

  def getExecutionOutput(executionId: String): StageOutput = {
    executionsMap(executionId)
  }

  def getExecutionsOutput: Map[String, StageOutput] = {
    executionsMap.toMap
  }

}
