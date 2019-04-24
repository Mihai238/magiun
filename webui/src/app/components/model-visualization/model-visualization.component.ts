import {Component} from "@angular/core";
import {TrainAlgorithmResponse} from "../../model/response/train.algorithm.response.model";
import {MagiunLogger} from "../../util/magiun.logger";
import {NGXLogger} from "ngx-logger";
import {ModelService} from "../../services/model.service";
import {AlgorithmImplementation} from "app/model/algorithm/train/algorithm.implementation.model";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-model-visualization',
  templateUrl: './model-visualization.component.html',
  styleUrls: ['./model-visualization.component.scss']
})
export class ModelVisualizationComponent {

  private logger: MagiunLogger;
  AlgorithmImplementation = AlgorithmImplementation;
  models: TrainAlgorithmResponse[] = [];

  constructor(
    private modelService: ModelService,
    private translate: TranslateService,
    ngxLogger: NGXLogger
  ) {
    this.logger = new MagiunLogger(ModelVisualizationComponent.name, ngxLogger);
    this.models = this.modelService.models;
    this.modelService.change.subscribe(model => {
      if (this.models.indexOf(model) < 0) {
        this.models.push(model);
      }
    })
  }

}
