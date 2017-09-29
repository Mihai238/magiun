package at.magiun.core.config

import com.typesafe.config.Config
import slick.jdbc.H2Profile.api._

object H2Config {

  def create(config: Config): Database = Database.forConfig("h2mem", config)

}
