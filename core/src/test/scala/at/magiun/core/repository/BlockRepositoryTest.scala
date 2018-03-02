package at.magiun.core.repository

import at.magiun.core.{MainModule, TestData, UnitTest}

import scala.concurrent.Await

class BlockRepositoryTest extends UnitTest {

  private val mainModule = new MainModule {}
  private val repo = mainModule.blockRepository

  it should "insert a block" in {
    Await.result(repo.upsert(TestData.testBlockEntity1), TIMEOUT)
    val result = Await.result(repo.find("id-2"), TIMEOUT)

    result.id should be ("id-2")
    result.`type` should be ("FileReader")
  }

  it should "update a block" in {
    Await.result(repo.upsert(TestData.testBlockEntity1), TIMEOUT)
    Await.result(repo.upsert(TestData.testBlockEntity1.copy(`type` = "DatabaseReader")), TIMEOUT)
    val result = Await.result(repo.find("id-2"), TIMEOUT)

    result.id should be ("id-2")
    result.`type` should be ("DatabaseReader")
  }

}
