package at.magiun.core.rest

import at.magiun.core.service.BlockService
import at.magiun.core.{MainModule, TestData, UnitTest}
import io.finch.Input

import scala.concurrent.Future

class BlockControllerTest extends UnitTest {

  private val mainModule = new MainModule {
    override lazy val blockService: BlockService = stub[BlockService]
  }

  val stubService: BlockService = mainModule.blockService
  val controller: BlockController = mainModule.blockController

  it should "return a block" in {
    val input = Input.get("/blocks/2")
    stubService.find _ when 2 returns Future.successful(Option(TestData.testBlock2))

    val result = controller.getBlock(input)
    val block = result.awaitValueUnsafe().get

    block.id should be ("2")
    block.`type` should be ("")
  }

}
