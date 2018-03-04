import {Component} from '@angular/core';
import {BlockParameter} from '../../../block-parameter';
import {ParameterComponent} from '../parameterComponent';

@Component({
  selector: 'app-file-parameter',
  templateUrl: './file-parameter.component.html',
  styleUrls: ['./file-parameter.component.scss']
})
export class FileParameterComponent extends ParameterComponent {

  uploadFile(event, parameter: BlockParameter) {
    this.parameter.value = event.target.value;
    this.emitEvent();
  }

  private getFileUploadText() {
    if (this.parameter.value === null || this.parameter.value === undefined) {
      return 'WORKFLOWS.BLOCKS.PARAMETERS.UPLOAD_FILE_TEXT';
    } else {
      return this.parameter.value.split( '\\' ).pop();
    }
  }
}
