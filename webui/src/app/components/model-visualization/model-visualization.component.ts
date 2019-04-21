import {Component} from "@angular/core";
import {TrainAlgorithmResponse} from "../../model/response/train.algorithm.response.model";
import {MagiunLogger} from "../../util/magiun.logger";
import {NGXLogger} from "ngx-logger";

@Component({
  selector: 'app-model-visualization',
  templateUrl: './model-visualization.component.html',
  styleUrls: ['./model-visualization.component.scss']
})
export class ModelVisualizationComponent {

  private logger: MagiunLogger;
  models: TrainAlgorithmResponse[];

  constructor(ngxLogger: NGXLogger) {
    this.logger = new MagiunLogger(ModelVisualizationComponent.name, ngxLogger)
  }

}
