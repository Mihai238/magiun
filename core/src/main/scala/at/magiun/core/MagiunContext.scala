package at.magiun.core

import java.util.concurrent.TimeUnit

import at.magiun.core.model.Schema
import com.google.common.cache.CacheBuilder
import org.apache.spark.sql.DataFrame

class MagiunContext {

  /** the current dataFrame and magiunDataset which are in use */
  private lazy val dataFrameCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).maximumSize(1).build[String, DataFrame]()
  private lazy val magiunSchemaCache = CacheBuilder.newBuilder().expireAfterWrite(15, TimeUnit.MINUTES).maximumSize(1).build[String, Schema]()

  def addDataFrameToCache(id: String, dataFrame: DataFrame): Unit = {
    dataFrameCache.put(id, dataFrame)
  }

  def addMagiunSchemaToCache(id: String, magiunDataSet: Schema): Unit = {
    magiunSchemaCache.put(id, magiunDataSet)
  }

  def getDataFrame(id: String): Option[DataFrame] = {
    Option(dataFrameCache.getIfPresent(id))
  }

  def getMagiunSchema(id: String): Option[Schema] = {
    Option(magiunSchemaCache.getIfPresent(id))
  }

  def sayHello(): Unit = {
    println("Hello!")
  }

}
