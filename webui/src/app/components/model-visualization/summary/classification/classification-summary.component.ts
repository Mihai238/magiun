import {Component} from "@angular/core";
import {SummaryComponent} from "../summary.component";
import {DialogService} from "ng2-bootstrap-modal";
import {AlgorithmImplementation} from "../../../../model/algorithm/train/algorithm.implementation.model";

@Component({
  selector: 'app-classification-summary',
  templateUrl: 'classification-summary.component.html',
  styleUrls:['classification-summary.component.scss']
})
export class ClassificationSummaryComponent extends SummaryComponent{

  AlgorithmImplementation = AlgorithmImplementation;

  constructor(dialogService: DialogService) {
    super(dialogService);
  }

}
