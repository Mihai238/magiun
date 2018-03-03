import {Component} from '@angular/core';
import {DialogComponent, DialogService} from 'ng2-bootstrap-modal';
import {BlockParameter} from '../block-parameter';

export interface ParametersModal {
  parameters: Array<BlockParameter>
}

@Component({
  selector: 'app-parameters-modal',
  templateUrl: './parameters-modal.component.html',
  styleUrls: ['./parameters-modal.component.scss']
})
export class ParametersModalComponent extends DialogComponent<ParametersModal, Array<BlockParameter>> implements ParametersModal {

  parameters: Array<BlockParameter>;

  constructor(dialogService: DialogService) {
    super(dialogService);
  }

  confirm() {
    this.result = this.parameters;
    this.close();
  }

  private parameterValueChanged(event) {
    const p = event as BlockParameter;
    this.parameters[this.parameters.indexOf(p)] = p;
  }
}
