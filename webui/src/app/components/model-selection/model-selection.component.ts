import { Component } from '@angular/core';
import {NGXLogger} from "ngx-logger";

@Component({
  selector: 'app-model-selection',
  templateUrl: './model-selection.component.html',
  styleUrls: ['./model-selection.component.scss']
})
export class ModelSelectionComponent {

  constructor(private logger: NGXLogger) {
    this.logger.info("ModelSelectionComponent: created!")
  }

}
