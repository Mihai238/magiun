import {Component} from "@angular/core";
import {ParameterComponent} from "../parameterComponent";



@Component({
  selector: 'app-multi-input-parameter',
  templateUrl: './multi-input-parameter.component.html'
})
export class MultiInputParameterComponent extends ParameterComponent {

  onInputChange() {
    this.emitEvent();
  }

}
