package at.magiun.core

import com.twitter.finagle.Http
import com.twitter.util.Await

object App {

  def main(args: Array[String]): Unit = {
    val mainModule = new MainModule{}
    mainModule.databaseInitializer.init()

    Await.ready(Http.server.serve(":8080", mainModule.restApi.corsService))
  }

}
