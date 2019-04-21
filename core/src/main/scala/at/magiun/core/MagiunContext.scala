package at.magiun.core

import java.util.concurrent.TimeUnit

import at.magiun.core.model.Schema
import com.google.common.cache.CacheBuilder
import org.apache.spark.sql.DataFrame

class MagiunContext {

  /** the current dataFrame and magiunDataset which are in use */
  private lazy val dataFrameCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).maximumSize(1).build[String, DataFrame]()
  private lazy val magiunSchemaCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).maximumSize(1).build[String, Schema]()
  private lazy val predictionsCache = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).maximumSize(15).build[String, DataFrame]()
  private lazy val residualsCache = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).maximumSize(15).build[String, DataFrame]()

  def addDataFrameToCache(id: String, dataFrame: DataFrame): Unit = {
    dataFrameCache.put(id, dataFrame)
  }

  def addMagiunSchemaToCache(id: String, magiunDataSet: Schema): Unit = {
    magiunSchemaCache.put(id, magiunDataSet)
  }

  def addPredictionToCache(id: String, prediction: DataFrame): Unit = {
    predictionsCache.put(id, prediction)
  }

  def addResidualsToCache(id: String, residuals: DataFrame): Unit = {
    residualsCache.put(id, residuals)
  }

  def getDataFrame(id: String): Option[DataFrame] = {
    Option(dataFrameCache.getIfPresent(id))
  }

  def getMagiunSchema(id: String): Option[Schema] = {
    Option(magiunSchemaCache.getIfPresent(id))
  }

  def getResiduals(id: String): Option[DataFrame] = {
    Option(residualsCache.getIfPresent(id))
  }

  def getPredictions(id: String): Option[DataFrame] = {
    Option(predictionsCache.getIfPresent(id))
  }

  def sayHello(): Unit = {
    println("Hello!")
  }

}
