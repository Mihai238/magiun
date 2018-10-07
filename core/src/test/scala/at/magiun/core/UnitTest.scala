package at.magiun.core

import org.junit.runner.RunWith
import org.scalamock.scalatest.MockFactory
import org.scalatest._
import org.scalatest.junit.JUnitRunner

import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
abstract class UnitTest extends FlatSpec with Matchers with MockFactory {

  protected val TIMEOUT: FiniteDuration = 10.seconds

}
