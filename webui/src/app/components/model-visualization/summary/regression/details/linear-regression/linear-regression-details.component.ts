import {Component, Input} from "@angular/core";
import {TrainAlgorithmResponse} from "../../../../../../model/response/train.algorithm.response.model";

@Component({
  selector: 'app-linear-regression-details',
  templateUrl: './linear-regression-details.component.html',
  styleUrls: ['./linear-regression-details.component.scss']
})
export class LinearRegressionDetailsComponent {

  @Input() model: TrainAlgorithmResponse;
  numberFormat: string = '1.2-3';

}
