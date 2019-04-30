import {Component, Input} from "@angular/core";
import {TrainAlgorithmResponse} from "../../../../../../model/response/train.algorithm.response.model";

@Component({
  selector: 'app-tree-classification-details',
  templateUrl: 'tree-classification-details.component.html',
  styleUrls: ['tree-classification-details.component.scss']
})
export class TreeClassificationDetailsComponent {

  @Input() model: TrainAlgorithmResponse;
  numberFormat: string = '1.2-3';
}
