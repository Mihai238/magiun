package at.magiun.core.repository

/**
  *
  * @param id id
  * @param `type` see [[at.magiun.core.model.BlockType]]
  * @param config inputs + params from [[at.magiun.core.model.Block]]
  */
final case class BlockEntity(id: String, `type`: String, config: String) {

}
