package at.magiun.core

import java.util.concurrent.TimeUnit

import at.magiun.core.model.Schema
import at.magiun.core.model.algorithm.Algorithm
import at.magiun.core.model.rest.request.RecommenderRequest
import com.google.common.cache.CacheBuilder
import org.apache.spark.ml.{Estimator, Model}
import org.apache.spark.ml.util.MLWritable
import org.apache.spark.sql.DataFrame

class MagiunContext {

  /** the current dataFrame and magiunDataset which are in use */
  private lazy val dataFrameCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).maximumSize(1).build[String, DataFrame]()
  private lazy val magiunSchemaCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).maximumSize(1).build[String, Schema]()
  private lazy val modelsCache = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).maximumSize(15).build[String, Model[_ <: Model[_]] with MLWritable]()
  private lazy val recommenderRequestCache = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).maximumSize(15).build[String, RecommenderRequest]()
  private lazy val recommendationsCache = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).maximumSize(35).build[String, Algorithm[_ <: Estimator[_ <: Model[_ <: Model[_]]]]]()

  def addDataFrameToCache(id: String, dataFrame: DataFrame): Unit = {
    dataFrameCache.put(id, dataFrame)
  }

  def addMagiunSchemaToCache(id: String, magiunDataSet: Schema): Unit = {
    magiunSchemaCache.put(id, magiunDataSet)
  }

  def addModelToCache(id: String, model: Model[_ <: Model[_]] with MLWritable): Unit = {
    modelsCache.put(id, model)
  }

  def getDataFrame(id: String): Option[DataFrame] = {
    Option(dataFrameCache.getIfPresent(id))
  }

  def getMagiunSchema(id: String): Option[Schema] = {
    Option(magiunSchemaCache.getIfPresent(id))
  }

  def getModel(id: String): Option[Model[_ <: Model[_]] with MLWritable] = {
    Option(modelsCache.getIfPresent(id))
  }

  def removeModel(id: String): Unit = {
    modelsCache.invalidate(id)
  }

  def addRecommenderRequest(request: RecommenderRequest): Unit = {
    recommenderRequestCache.put(request.uid, request)
  }

  def getRecommenderRequest(id: String): Option[RecommenderRequest] = {
    Option(recommenderRequestCache.getIfPresent(id))
  }

  def addRecommendation(recommendation: Algorithm[_ <: Estimator[_ <: Model[_ <: Model[_]]]]): Unit = {
    recommendationsCache.put(recommendation.uid, recommendation)
  }

  def getRecommendation(id: String): Option[Algorithm[_ <: Estimator[_ <: Model[_ <: Model[_]]]]] = {
    Option(recommendationsCache.getIfPresent(id))
  }

  def sayHello(): Unit = {
    println("Hello!")
  }

}
