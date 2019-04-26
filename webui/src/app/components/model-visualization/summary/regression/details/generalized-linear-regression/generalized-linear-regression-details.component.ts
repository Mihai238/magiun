import {Component, Input} from "@angular/core";
import {TrainAlgorithmResponse} from "../../../../../../model/response/train.algorithm.response.model";

@Component({
  selector: 'app-generalized-linear-regression-details',
  templateUrl: './generalized-linear-regression-details.component.html',
  styleUrls: ['./generalized-linear-regression-details.component.scss']
})
export class GeneralizedLinearRegressionDetailsComponent {

  @Input() model: TrainAlgorithmResponse;
  numberFormat: string = '1.2-3';

}
