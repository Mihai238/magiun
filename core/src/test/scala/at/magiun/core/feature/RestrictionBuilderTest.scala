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
    restrictions("FalseValue").check("0") should be (true)
//    restrictions("TrueValue").check("true") should be (true)
    restrictions("TrueValue").check("1") should be (true)
    restrictions("FalseValue").check("0") should be (true)
  }

}
