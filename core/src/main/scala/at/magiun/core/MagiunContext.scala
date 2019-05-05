package at.magiun.core

import java.util.concurrent.TimeUnit

import at.magiun.core.model.Schema
import com.google.common.cache.CacheBuilder
import org.apache.spark.ml.Model
import org.apache.spark.ml.util.MLWritable
import org.apache.spark.sql.DataFrame

class MagiunContext {

  /** the current dataFrame and magiunDataset which are in use */
  private lazy val dataFrameCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).maximumSize(1).build[String, DataFrame]()
  private lazy val magiunSchemaCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).maximumSize(1).build[String, Schema]()
  private lazy val modelsCache = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).maximumSize(15).build[String, Model[_ <: Model[_]] with MLWritable]()

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

  def getModel(id: String): Model[_ <: Model[_]] with MLWritable = {
    modelsCache.getIfPresent(id)
  }

  def removeModel(id: String): Unit = {
    modelsCache.invalidate(id)
  }

  def sayHello(): Unit = {
    println("Hello!")
  }

}
