package at.magiun.core.rest

import at.magiun.core.service.StageService
import io.finch._

class StageController(stageService: StageService) {

  val api: Endpoint[String] = get("hello") {
    Ok("Hello World!")
  }

}
