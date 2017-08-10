package at.magiun.core.rest

import io.finch._

class OtherController {

  val api: Endpoint[String] = get("hello") {
    Ok("Hello World!")
  }

}
