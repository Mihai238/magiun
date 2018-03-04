import {Component} from '@angular/core';
import {DialogComponent, DialogService} from 'ng2-bootstrap-modal';
import {BlockParameter} from '../block-parameter';

export interface ParametersModal {
  parameters: Array<BlockParameter<any>>
}

@Component({
  selector: 'app-parameters-modal',
  templateUrl: './parameters-modal.component.html',
  styleUrls: ['./parameters-modal.component.scss']
})
export class ParametersModalComponent extends DialogComponent<ParametersModal, Array<BlockParameter<any>>> implements ParametersModal {

  parameters: Array<BlockParameter<any>>;

  constructor(dialogService: DialogService) {
    super(dialogService);
  }

  confirm() {
    this.result = this.parameters;
    this.close();
  }

  private parameterValueChanged(event) {
    const p = event as BlockParameter<any>;
    this.deleteDependenciesIfNeeded(p);
    this.replaceOldParameterValue(p);
    this.addDependenciesIfNeeded(p);
  }

  private replaceOldParameterValue(newParameter: BlockParameter<any>) {
    this.parameters.filter(p => p.equals(newParameter)).forEach(p => p.value = newParameter.value);
  }

  private deleteDependenciesIfNeeded(p: BlockParameter<any>) {
    if (p.htmlType === 'select') {
      const oldParamValue = this.parameters.filter(p2 => p2.equals(p))[0].value;
      if (oldParamValue.dependencies !== undefined && oldParamValue.dependencies !== null) {
        oldParamValue.dependencies.forEach(d => {
          if (d.htmlType === 'option') {
            d.dependencies.forEach(dd => {
              this.parameters.splice(this.parameters.indexOf(dd), 1)
            });
          } else {
            this.parameters.splice(this.parameters.indexOf(d), 1)
          }
        });
      }
    }
  }

  private addDependenciesIfNeeded(p: BlockParameter<any>) {
    if (p.htmlType === 'select') {
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
}
