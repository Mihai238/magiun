import {BlockComponent} from './block.component';

export class BlockComponentsRelation {

  constructor(
    readonly component1: BlockComponent,
    readonly component1OutputIndex: number,
    readonly component2: BlockComponent,
    readonly component2InputIndex: number,
    readonly line: any
  ) {}

}
