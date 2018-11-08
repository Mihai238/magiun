import {AfterViewChecked, Component} from '@angular/core';
import {NGXLogger} from "ngx-logger";
import {DataService} from "../../services/data.service";

@Component({
  selector: 'app-model-selection',
  templateUrl: './model-selection.component.html',
  styleUrls: ['./model-selection.component.scss']
})
export class ModelSelectionComponent implements AfterViewChecked {

  private data;

  constructor(
    private dataService: DataService,
    private logger: NGXLogger
  ) {
    this.logger.info("ModelSelectionComponent: created!")
  }

  ngAfterViewChecked(): void {
    this.logger.info("ModelSelectionComponent: view selected!");
    this.logger.info("ModelSelectionComponent: trying to retrieve data from data component!");
  }

}
