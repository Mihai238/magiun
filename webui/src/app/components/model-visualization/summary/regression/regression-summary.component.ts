import {Component} from "@angular/core";
import {SummaryComponent} from "../summary.component";
import {DialogService} from "ng2-bootstrap-modal";

@Component({
  selector: 'app-regression-summary',
  templateUrl: './regression-summary.component.html',
  styleUrls: ['./regression-summary.component.scss']
})
export class RegressionSummaryComponent extends SummaryComponent {

  constructor(dialogService: DialogService) {
    super(dialogService);
  }

}
