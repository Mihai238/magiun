package at.magiun.core.rest

import io.finch._

class UserController {

  //noinspection TypeAnnotation
  lazy val api = getUser :+: getUsers

  val getUser: Endpoint[PersonDto] = get("users" :: path[Int]) { id: Int =>
    Ok(PersonDto("John"))
  }

  val getUsers: Endpoint[List[PersonDto]] = get("users") {
    Ok(
      List(
        PersonDto("John"),
        PersonDto("Nechifor")
      )
    )
  }

  case class PersonDto(name: String)

}