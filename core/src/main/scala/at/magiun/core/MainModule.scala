package at.magiun.core

import com.softwaremill.macwire._
import io.finch._
import com.twitter.finagle.Http
import com.twitter.util.Await

//noinspection TypeAnnotation
object MainModule {

  lazy val magiunContext = wire[MagiunContext]
  lazy val exampleController = wire[ExampleController]

  def main(args: Array[String]): Unit = {
    magiunContext.sayHello()

    Await.ready(Http.server.serve(":8080", exampleController.api.toServiceAs[Text.Plain]))
  }

}
