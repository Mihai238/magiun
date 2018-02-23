package at.magiun.core

import org.junit.runner.RunWith
import org.scalamock.scalatest.MockFactory
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
abstract class UnitTest extends FlatSpec with Matchers with MockFactory {

}
