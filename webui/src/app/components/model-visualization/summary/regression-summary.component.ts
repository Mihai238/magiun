import {Component, Input} from "@angular/core";
import {TrainAlgorithmResponse} from "../../../model/response/train.algorithm.response.model";

@Component({
  selector: 'app-regression-summary',
  templateUrl: "./regression-summary.component.html",
  styleUrls: ['./regression-summary.component.scss']
})
export class RegressionSummaryComponent {

  @Input() model: TrainAlgorithmResponse;

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

  }
}
