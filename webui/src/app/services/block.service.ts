import {Injectable} from '@angular/core';
import {BlockComponent} from '../components/workflows/blocks/block.component';

declare var LeaderLine: any;

@Injectable()
export class BlockService {

  private startComponent: BlockComponent;
  private startId: string;

  startLine(component: BlockComponent, startId: string): void {
    if (this.startId === null || this.startId === undefined) {
      this.startComponent = component;
      this.startId = startId;

      const e = document.getElementById(startId);
      if (e.classList.contains('unset')) {
        this.changeFromUnsetToSelected(e);
      }
    }
  }

  endLine(endComponent: BlockComponent, endId: string): void {
    if (this.startId !== endId && this.startComponent !== endComponent) {
      new LeaderLine(
        document.getElementById(this.startId),
        document.getElementById(endId)
      );

      this.changeFromSelectedToSet(document.getElementById(this.startId));
      this.changeFromUnsetToSet(document.getElementById(endId));
      this.reset();
    }
  }

  private changeFromUnsetToSelected(e) {
    e.classList.remove('unset');
    e.classList.add('selected')
  }

  private changeFromSelectedToSet(e) {
    e.classList.remove('selected');
    e.classList.add('set');
  }

  private changeFromUnsetToSet(e) {
    e.classList.remove('unset');
    e.classList.add('set');
  }

  private reset(): void {
    this.startId = null;
    this.startComponent = null;
  }

}
