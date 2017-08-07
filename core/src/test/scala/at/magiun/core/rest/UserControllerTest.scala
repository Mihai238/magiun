package at.magiun.core.rest

import io.finch.Input
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class UserControllerTest extends FlatSpec with Matchers {

  it should "test a simple test :)" in {
    val input = Input.get("/users/1")

    val result = new UserController().getUser(input)

    result.awaitValueUnsafe().get should be (UserDto("John"))
  }

}
