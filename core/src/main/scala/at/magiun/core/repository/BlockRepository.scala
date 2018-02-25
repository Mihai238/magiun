package at.magiun.core.repository

import at.magiun.core.model.Block
import slick.jdbc.H2Profile.api._

class BlockRepository(db: Database) {

  def getBlock(id: Long): Block = {
    ???
  }

}