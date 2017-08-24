package at.magiun.core.rest

import io.finch._
import org.apache.spark.sql.SparkSession

class UserController(spark: SparkSession) {

  //noinspection TypeAnnotation
  lazy val api = getUser :+: getUsers

  println(spark.conf.getAll)

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