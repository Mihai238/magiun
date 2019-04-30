import {Component, Input} from "@angular/core";
import {TrainAlgorithmResponse} from "../../../../../../model/response/train.algorithm.response.model";

@Component({
  selector: 'app-classification-intercept-measurements',
  templateUrl: 'classification-intercept-measurements.component.html',
  styleUrls: ['classification-intercept-measurements.component.scss']
})
export class ClassificationInterceptMeasurementsComponent {

  @Input() model: TrainAlgorithmResponse;
  numberFormat: string = '1.2-3';
}
