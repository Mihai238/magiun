package at.magiun.core.rest

import at.magiun.core.{MainModule, UnitTest}
import io.finch.Input

class UserControllerTest extends UnitTest {

  private val mainModule = new MainModule {}

  it should "test a simple test :)" in {
    val input = Input.get("/users/1")

    val result = new UserController(mainModule.spark).getUser(input)

    result.awaitValueUnsafe().get should be (UserDto("John"))
  }

}
