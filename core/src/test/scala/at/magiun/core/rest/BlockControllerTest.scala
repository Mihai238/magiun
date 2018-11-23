package at.magiun.core.rest

import at.magiun.core.model.BlockType.FileReader
import at.magiun.core.model.{Block, BlockType}
import at.magiun.core.service.BlockService
import at.magiun.core.{MainModule, TestData, UnitTest}
import com.twitter.io.Buf
import io.finch.{Application, Input}

import scala.concurrent.Future

class BlockControllerTest extends UnitTest {

  private val mainModule = new MainModule {
    override lazy val blockService: BlockService = stub[BlockService]
  }

  val stubService: BlockService = mainModule.blockService
  val controller: BlockController = mainModule.blockController

  it should "read a block" in {
    val input = Input.get("/blocks/2")
    stubService.find _ when "2" returns Future.successful(TestData.testBlock2)

    val result = controller.getBlock(input)
    val block = result.awaitValueUnsafe().get

    block.id should be("id-2")
    block.`type` should be(BlockType.FileReader)
  }

  it should "create a block" in {
    val input = Input.post("/blocks/").withBody[Application.Json](Buf.Utf8("""{"id":"id-2","type":"FileReader","inputs":[{"blockId":"1","index":0}],"params":{"x":"4"}}"""))
    val result = controller.upsertBlock(input)
    stubService.upsert _ when * returns Future.successful(TestData.testBlock2)

    val block = result.awaitValueUnsafe().get
    block.id should be ("id-2")

    val matcher = where {
      b: Block => b.id == "id-2" && b.`type` == FileReader && b.inputs.head.blockId == "1"
    }

    stubService.upsert _ verify matcher
  }

}
