import {Component} from '@angular/core';
import {ParameterComponent} from '../parameterComponent';

@Component({
  selector: 'app-select-component',
  templateUrl: './select-parameter.component.html',
  styleUrls: ['./select-parameter.component.scss']
})
export class SelectParameterComponent extends ParameterComponent {

  private selectionChanged(newSelection): void {
    this.parameter.value = this.parameter.dependencies.filter(d => d.name === newSelection)[0];
    this.emitEvent();
  }
}
