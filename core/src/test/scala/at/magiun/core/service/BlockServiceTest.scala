package at.magiun.core.service

import at.magiun.core.TestData._
import at.magiun.core.model.{Block, BlockType}
import at.magiun.core.repository.{BlockEntity, BlockRepository}
import at.magiun.core.{MainModule, UnitTest}

import scala.concurrent.{Await, Future}

class BlockServiceTest extends UnitTest {

  private val mainModule = new MainModule {
    override lazy val blockRepository: BlockRepository = stub[BlockRepository]
  }

  private val service = mainModule.blockService
  private val mockedRepo = mainModule.blockRepository

  it should "return a block" in {
    mockedRepo.find _ when "id-2" returns Future.successful(Option(testBlockEntity1))

    val result = Await.result(service.find("id-2"), TIMEOUT).get

    result.id should be("id-2")
    result.`type` should be(BlockType.FileReader)
    result.inputs should be(Seq(("1", 0)))
    result.params should be(Map("x" -> "4"))
  }

  it should "upsert a block" in {
    mockedRepo.upsert _ when * returns Future.successful(testBlockEntity1)

    val result = Await.result(service.upsert(testBlock2), TIMEOUT)

    val matcher = where {
      (b: BlockEntity) => b.id == "id-2" && b.config == """{"inputs":[["1",0]],"params":{"x":"4"}}"""
    }
    mockedRepo.upsert _ verify matcher
  }

}
