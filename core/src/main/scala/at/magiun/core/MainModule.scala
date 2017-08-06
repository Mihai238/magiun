package at.magiun.core

import com.softwaremill.macwire._

//noinspection TypeAnnotation
object MainModule {

  lazy val magiunContext = wire[MagiunContext]

  def main(args: Array[String]): Unit = {
    magiunContext.sayHello()
  }

}
