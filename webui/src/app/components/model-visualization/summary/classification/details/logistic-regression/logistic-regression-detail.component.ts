import {Component, Input} from "@angular/core";
import {TrainAlgorithmResponse} from "../../../../../../model/response/train.algorithm.response.model";

@Component({
  selector: 'app-logistic-regression-detail',
  templateUrl: 'logistic-regression-detail.component.html',
  styleUrls: ['logistic-regression-detail.component.scss']
})
export class LogisticRegressionDetailComponent {

  @Input() model: TrainAlgorithmResponse;
  numberFormat: string = '1.2-3';
}
