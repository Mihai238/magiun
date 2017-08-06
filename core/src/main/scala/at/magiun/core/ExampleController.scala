package at.magiun.core

import io.finch._

class ExampleController {

  val api: Endpoint[String] = get("hello") {
    Ok("Hello World!")
  }

}
