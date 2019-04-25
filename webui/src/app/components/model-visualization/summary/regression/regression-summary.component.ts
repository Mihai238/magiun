import {Component, Input} from "@angular/core";
import {TrainAlgorithmResponse} from "../../../../model/response/train.algorithm.response.model";
import {DialogService} from "ng2-bootstrap-modal";
import {RegressionPlotsModalComponent} from "../../plots/regression-plots-modal/regression-plots-modal.component";
import {StatisticsUtils} from "../../../../util/statistics.utils";

@Component({
  selector: 'app-regression-summary',
  templateUrl: "./regression-summary.component.html",
  styleUrls: ['./regression-summary.component.scss']
})
export class RegressionSummaryComponent {

  @Input() model: TrainAlgorithmResponse;
  Math = Math;
  StatisticsUtils = StatisticsUtils;
  numberFormat: string = '1.2-3';

  constructor(private dialogService: DialogService) {

  }


  getSignificanceCode(value: number): String {
    if (value >= 0 && value < 0.001) {
      return "***";
    } else if (value >= 0.001 && value < 0.01) {
      return "**"
    } else if (value >= 0.01 && value < 0.05) {
      return "*";
    } else if (value >= 0.05 && value < 0.1) {
      return ".";
    } else {
      return " ";
    }
  }

  plot(): void {
    this.dialogService.addDialog(
      RegressionPlotsModalComponent,
      {
        residuals: this.model.residuals,
        fittedValues: this.model.fittedValues,
        dataSample: this.model.dataSample
      }
    ).subscribe();
  }
}
