import {Component, Input} from "@angular/core";
import {TrainAlgorithmResponse} from "../../../../../../model/response/train.algorithm.response.model";

@Component({
  selector: 'app-tree-regression-details',
  templateUrl: './tree-regression-details.component.html',
  styleUrls: ['./tree-regression-details.component.scss']
})
export class TreeRegressionDetailsComponent {

  @Input() model: TrainAlgorithmResponse;
  numberFormat: string = '1.2-3';

}
