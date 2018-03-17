import {Component} from "@angular/core";
import {ParameterComponent} from "../parameterComponent";
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/map';

@Component({
  selector: 'app-multi-input-parameter',
  templateUrl: './multi-input-parameter.component.html'
})
export class MultiInputParameterComponent extends ParameterComponent {

  onInputChange() {
    this.emitEvent();
  }

}
