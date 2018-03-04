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
    this.deleteDependenciesIfNeeded(p);
    this.parameters[this.parameters.indexOf(p)] = p;
    this.addDependenciesIfNeeded(p);
  }

  private deleteDependenciesIfNeeded(p: BlockParameter) {
    const oldParamValue = this.parameters[this.parameters.indexOf(p)].value;
    if (oldParamValue.dependencies !== undefined && oldParamValue.dependencies !== null) {
      oldParamValue.dependencies.forEach(d => {
        if (d.htmlType === 'option') {
          d.dependencies.forEach(dd => this.parameters.splice(this.parameters.indexOf(dd), 1))
        } else {
          this.parameters.splice(this.parameters.indexOf(d), 1)
        }
      });
    }
  }

  private addDependenciesIfNeeded(p: BlockParameter) {
    if (p.value.dependencies !== null && p.value.dependencies !== undefined) {
      p.value.dependencies.forEach(d => {
        if (d.htmlType === 'option') {
          d.dependencies.forEach(dd => this.parameters.push(dd));
        } else {
          this.parameters.push(d);
        }
      });
    }
  }
}
