import {ParameterComponent} from '../parameterComponent';
import {Component} from '@angular/core';

@Component({
  selector: 'app-checkbox-parameter',
  templateUrl: './checkbox-parameter.component.html',
  styleUrls: ['./checkbox-parameter.component.scss']
})
export class CheckboxParameterComponent extends ParameterComponent {

  inputChanged(event) {
    this.parameter.value = event.target.checked;
    this.emitEvent();
  }
}
