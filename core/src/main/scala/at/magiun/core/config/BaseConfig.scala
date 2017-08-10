package at.magiun.core.config

import com.typesafe.config.{Config, ConfigFactory}

class BaseConfig {

  val config: Config = ConfigFactory.load()
}
