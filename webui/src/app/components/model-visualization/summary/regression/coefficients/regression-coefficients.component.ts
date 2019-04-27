import {Component, Input} from "@angular/core";
import {TrainAlgorithmResponse} from "../../../../../model/response/train.algorithm.response.model";
import {StatisticsUtils} from "../../../../../util/statistics.utils";

@Component({
  selector: 'app-regression-coefficients',
  templateUrl: './regression-coefficients.component.html',
  styleUrls: ['./regression-coefficients.component.scss']
})
export class RegressionCoefficientsComponent {

  @Input() model: TrainAlgorithmResponse;

  Math = Math;
  StatisticsUtils = StatisticsUtils;
  numberFormat: string = '1.2-3';

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
}
