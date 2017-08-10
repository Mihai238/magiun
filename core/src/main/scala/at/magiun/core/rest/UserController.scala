package at.magiun.core.rest

import io.finch._

class UserController {

  //noinspection TypeAnnotation
  lazy val api = getUser :+: getUsers

  val getUser: Endpoint[UserDto] = get("users" :: path[Int]) { id: Int =>
    Ok(UserDto("John"))
  }

  val getUsers: Endpoint[List[UserDto]] = get("users") {
    Ok(
      List(
        UserDto("John"),
        UserDto("Nechifor")
      )
    )
  }

}

case class UserDto(name: String)