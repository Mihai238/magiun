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

  uploadFile(event, parameter: BlockParameter) {
    this.parameters[this.parameters.indexOf(parameter)].value = event.target.value;
  }

  private getFileUploadText(parameter: BlockParameter) {
    if (parameter.value === null || parameter.value === undefined) {
      return 'WORKFLOWS.BLOCKS.PARAMETERS.UPLOAD_FILE_TEXT';
    } else {
      return parameter.value.split( '\\' ).pop();
    }

  }
}
