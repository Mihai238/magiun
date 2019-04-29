import {Component} from "@angular/core";
import {TrainAlgorithmResponse} from "../../model/response/train.algorithm.response.model";
import {MagiunLogger} from "../../util/magiun.logger";
import {NGXLogger} from "ngx-logger";
import {ModelService} from "../../services/model.service";
import {AlgorithmImplementation} from "app/model/algorithm/train/algorithm.implementation.model";
import {TranslateService} from "@ngx-translate/core";
import {AlgorithmRestService} from "../../rest/algorithm-rest.service";
import {NotifierService} from "angular-notifier";
import {CollectionsUtils} from "../../util/collections.utils";

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
    private algorithmRestService: AlgorithmRestService,
    private notifier: NotifierService,
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

  save(id: string): void {
    console.log("save model with id " + id);
  }

  delete(id: string): void {
    console.log("delete model with id " + id);
    this.algorithmRestService.remove(id).subscribe(() => {
      let model = this.models.filter(m => m.id == id)[0];
      this.models = CollectionsUtils.withoutElement(this.models, model);
      this.notifier.notify('success', this.translate.instant("MODEL_VISUALIZATION.MODEL_WAS_DELETED"));
    });
  }

}
