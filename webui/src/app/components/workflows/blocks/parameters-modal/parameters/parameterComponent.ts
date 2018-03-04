import {EventEmitter, Input, Output} from '@angular/core';
import {BlockParameter} from '../../block-parameter';

export class ParameterComponent {
  @Input() parameter: BlockParameter<any>;
  @Output('onValueChanged') onValueChanged = new EventEmitter<any>();

  protected emitEvent() {
    this.onValueChanged.emit(this.parameter);
  }

  getDependencies(): Array<BlockParameter<any>> {
      return this.parameter.value.dependencies;
  }
}
