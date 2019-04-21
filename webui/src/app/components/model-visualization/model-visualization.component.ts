import {Component} from "@angular/core";
import {TrainAlgorithmResponse} from "../../model/response/train.algorithm.response.model";
import {MagiunLogger} from "../../util/magiun.logger";
import {NGXLogger} from "ngx-logger";
import {ModelService} from "../../services/model.service";

@Component({
  selector: 'app-model-visualization',
  templateUrl: './model-visualization.component.html',
  styleUrls: ['./model-visualization.component.scss']
})
export class ModelVisualizationComponent {

  private logger: MagiunLogger;
  models: TrainAlgorithmResponse[];

  constructor(
    private modelService: ModelService,
    ngxLogger: NGXLogger
  ) {
    this.logger = new MagiunLogger(ModelVisualizationComponent.name, ngxLogger);
    this.models = this.modelService.models;
    this.modelService.change.subscribe(model => {
      this.models.push(model);
    })
  }

}
