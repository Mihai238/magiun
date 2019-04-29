import {Component} from "@angular/core";
import {SummaryComponent} from "../summary.component";
import {DialogService} from "ng2-bootstrap-modal";

@Component({
  selector: 'app-classification-summary',
  templateUrl: 'classification-summary.component.html',
  styleUrls:['classification-summary.component.scss']
})
export class ClassificationSummaryComponent extends SummaryComponent{

  constructor(dialogService: DialogService) {
    super(dialogService);
  }

}
