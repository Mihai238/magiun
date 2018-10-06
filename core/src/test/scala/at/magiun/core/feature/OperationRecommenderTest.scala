package at.magiun.core.feature

import at.magiun.core.{MainModule, UnitTest}
import org.scalatest.FunSuite

class OperationRecommenderTest extends UnitTest {

  private val mainModule = new MainModule {}
  private val opRecommender = mainModule.operationRecommender

  it should "do something" in {
    val colTypes = Map[Int, List[String]](
      0 -> List("HumanAgeColumn")
    )

    opRecommender.recommend(colTypes)
  }

}
