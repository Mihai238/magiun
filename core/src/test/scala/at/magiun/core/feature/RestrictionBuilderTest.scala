package at.magiun.core.feature

import at.magiun.core.{MainModule, UnitTest}

class RestrictionBuilderTest extends UnitTest {

  private val mainModule = new MainModule {}

  private val restrictionBuilder = mainModule.restrictionBuilder
  private val model = mainModule.model

  it should "build shacl restricitons" in {
    val restrictions = restrictionBuilder.build(model)

    restrictions("GenderValue").check("Male") should be (true)
    restrictions("GenderValue").check("male") should be (true)
    restrictions("GenderValue").check("ff") should be (false)
    restrictions("GenderValue").check("female") should be (true)
    restrictions("GenderValue").check("m") should be (true)

    restrictions("HumanAgeValue").check(1) should be (true)
    restrictions("HumanAgeValue").check(88) should be (true)
    restrictions("HumanAgeValue").check("88") should be (true)
    restrictions("HumanAgeValue").check("88.2") should be (true)
    restrictions("HumanAgeValue").check(-5) should be (false)

    restrictions("BooleanValue").check(0) should be (true)
    restrictions("BooleanValue").check(1) should be (true)
    restrictions("BooleanValue").check("ffx") should be (false)
    restrictions("BooleanValue").check(42) should be (false)

    restrictions("IntValue").check(42) should be (true)
    restrictions("IntValue").check("1") should be (true)
    restrictions("IntValue").check("hello") should be (false)

    restrictions("NumericValue").check(42) should be (true)
    restrictions("NumericValue").check("1.8") should be (true)
    restrictions("NumericValue").check("hello") should be (false)

    restrictions("HumanEthnicityValue").check("asian") should be (true)
    restrictions("HumanEthnicityValue").check("miami") should be (false)

    restrictions("CountryValue").check("USA") should be (true)
    restrictions("CountryValue").check("asia") should be (false)

    restrictions("EmailValue").check("wrongEmail@") should be (false)
    restrictions("EmailValue").check("me@tuwien.ac.at") should be (true)
    restrictions("EmailValue").check("me@TUWIEN.ac.at") should be (true)

    restrictions("MoneyValue").check("12") should be (false)
    restrictions("MoneyValue").check("12 $") should be (true)
    restrictions("MoneyValue").check("12$") should be (true)
    restrictions("MoneyValue").check("12.2$") should be (true)
    restrictions("MoneyValue").check("12,2€") should be (true)
    restrictions("MoneyValue").check("€12,2") should be (true)
    restrictions("MoneyValue").check("€ 12,2") should be (true)

    restrictions("EducationValue").check("master degree") should be (true)
    restrictions("EducationValue").check("it") should be (false)

    restrictions("EmploymentStatusValue").check("Self-employed") should be (true)
    restrictions("EmploymentStatusValue").check("it") should be (false)

    restrictions("ProfessionValue").check("ff") should be (false)
    restrictions("ProfessionValue").check("software dev") should be (true)

    restrictions("YearValue").check("2000") should be (true)
    restrictions("YearValue").check("1") should be (false)
  }

}
