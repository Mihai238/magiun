import {Component, Input} from "@angular/core";
import {TrainAlgorithmResponse} from "../../../../../model/response/train.algorithm.response.model";

@Component({
  selector: 'app-classification-coefficients',
  templateUrl: 'classification-coefficients.component.html',
  styleUrls: ['classification-coefficients.component.scss']
})
export class ClassificationCoefficientsComponent {

  @Input() model: TrainAlgorithmResponse;
  numberFormat: string = '1.2-3';
}
