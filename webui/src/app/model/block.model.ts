export interface Block {
  id: string,
  type: string,
  inputs: BlockInput[],
  params: any
}

export interface BlockInput {
  blockId: string,
  index: number
}
