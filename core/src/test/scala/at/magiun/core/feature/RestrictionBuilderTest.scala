package at.magiun.core.feature

import at.magiun.core.{MainModule, UnitTest}

class RestrictionBuilderTest extends UnitTest {

  private val mainModule = new MainModule {}

  private val restrictionBuilder = mainModule.restrictionBuilder
  private val model = mainModule.model

  it should "build shacl restricitons" in {
    val restrictions = restrictionBuilder.build(model)

    restrictions("MaleValue").check("male") should be (true)
    restrictions("MaleValue").check("ff") should be (false)
    restrictions("FemaleValue").check("female") should be (true)
    restrictions("FemaleValue").check("m") should be (false)

    restrictions("HumanAgeValue").check(1) should be (true)
    restrictions("HumanAgeValue").check(88) should be (true)
    restrictions("HumanAgeValue").check("88") should be (true)
    restrictions("HumanAgeValue").check("88.2") should be (true)
    restrictions("HumanAgeValue").check(-5) should be (false)

    restrictions("BooleanValue").check(0) should be (true)
    restrictions("BooleanValue").check(1) should be (true)
    restrictions("BooleanValue").check("ffx") should be (false)
    restrictions("BooleanValue").check(42) should be (false)
  }

}
