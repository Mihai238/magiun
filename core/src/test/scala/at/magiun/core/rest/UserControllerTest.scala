package at.magiun.core.rest

import at.magiun.core.MainModule
import io.finch.Input
import org.junit.runner.RunWith
import org.scalamock.scalatest.MockFactory
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UserControllerTest extends FlatSpec with Matchers with MockFactory {

  private val mainModule = new MainModule {}

  it should "test a simple test :)" in {
    val input = Input.get("/users/1")

    val result = new UserController(mainModule.spark).getUser(input)

    result.awaitValueUnsafe().get should be (UserDto("John"))
  }

}
