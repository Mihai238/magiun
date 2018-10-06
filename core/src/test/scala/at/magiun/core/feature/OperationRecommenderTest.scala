package at.magiun.core.feature

import at.magiun.core.{MainModule, UnitTest}

class OperationRecommenderTest extends UnitTest {

  private val mainModule = new MainModule {}
  private val opRecommender = mainModule.operationRecommender

  it should "do something" in {
    val colTypes = Map[Int, List[String]](
      0 -> List("HumanAgeColumn"),
      1 -> List("CategoricalColumn", "BooleanColumn"),
      2 -> List("CategoricalColumn", "HumanAgeColumn")
    )

    val operations = opRecommender.recommend(colTypes)
    operations(0) should contain("Discretization")
    operations(1) should be(List.empty)
    operations(2) should contain("Discretization")
  }

}
