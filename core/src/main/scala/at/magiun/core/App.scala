package at.magiun.core

import com.twitter.finagle.Http
import com.twitter.util.Await
import com.typesafe.scalalogging.LazyLogging

object App extends LazyLogging {

  private val port = 8080

  def main(args: Array[String]): Unit = {
    val mainModule = new MainModule{}
    mainModule.databaseInitializer.init()

    val server = Http.server.serve(s":$port", mainModule.restApi.corsService)
    logger.info(s"App running on port $port")
    Await.ready(server)
  }

}
