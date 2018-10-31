package at.magiun.core.service

import at.magiun.core.TestData._
import at.magiun.core.feature.{Recommendation, Recommendations, Recommender}
import at.magiun.core.model.Execution
import at.magiun.core.repository.DataSetRepository
import at.magiun.core.{MainModule, UnitTest}

import scala.concurrent.{Await, Future}

class DataSetServiceTest extends UnitTest {

  private val mainModule = new MainModule {
    override lazy val dataSetRepository: DataSetRepository = stub[DataSetRepository]
    override lazy val blockService: BlockService = stub[BlockService]
    override lazy val recommender: Recommender = stub[Recommender]
  }

  private val service = mainModule.dataSetService
  private val executionService: ExecutionService = mainModule.executionService
  private val mockedRepo = mainModule.dataSetRepository
  private val mockedBlockService = mainModule.blockService
  private val mockedRecommender = mainModule.recommender

  it should "return a data set model" in {
    mockedRepo.find _ when 1 returns Future.successful(Option(testDsEntity1))

    val ds = Await.result(service.find("1"), TIMEOUT).get
    ds.name should be("gigi")
    ds.dataSetSource.url should be(testDsEntity1.url)
    ds.schema.get.columns should have size 12
  }

  it should "return rows from a memory ds" in {
    mockedBlockService.find _ when "id-3" returns Future.successful(fileReaderBlock)
    mockedBlockService.find _ when "id-2" returns Future.successful(dropColBlock)

    val execution = Await.result(executionService.execute(Execution(blockId = "id-2")), TIMEOUT)

    val rows = Await.result(service.findRows(execution.id), TIMEOUT).get
    rows should have size 891
  }

  it should "return recommendations" in {
    mockedRepo.find _ when 1 returns Future.successful(Option(testDsEntity1))
    mockedRecommender.recommend _ when * returns Recommendations(Map(0 -> Recommendation(List(), List("opRec"))))

    val recommendations = Await.result(service.getRecommendations("1"), TIMEOUT).get
    recommendations.map(0).colTypes should be(List.empty)
    recommendations.map(0).operations should contain("opRec")
  }

}
