import {Component} from '@angular/core';
import {ParameterComponent} from '../parameterComponent';

@Component({
  selector: 'app-input-parameter',
  templateUrl: './input-parameter.component.html',
  styleUrls: ['./input-parameter.component.scss']
})
export class InputParameterComponent extends ParameterComponent {

  inputChanged(event) {
    this.parameter.value = event.target.value;
    this.emitEvent();
  }

  inputType() {
    if (this.parameter.type === Number) {
      return 'number';
    } else {
      return 'textarea';
    }
  }
}
