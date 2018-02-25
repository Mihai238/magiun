package at.magiun.core.rest

import at.magiun.core.service.BlockService
import io.finch._

class BlockController(blockService: BlockService) {

  val api: Endpoint[String] = get("hello") {
    Ok("Hello World!")
  }

}
