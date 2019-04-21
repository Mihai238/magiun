import {EventEmitter, Injectable, Output} from "@angular/core";
import {MagiunLogger} from "../util/magiun.logger";
import {NGXLogger} from "ngx-logger";
import {TrainAlgorithmResponse} from "../model/response/train.algorithm.response.model";

@Injectable()
export class ModelService {
  private logger: MagiunLogger;
  models: TrainAlgorithmResponse[] = [];

  @Output() change: EventEmitter<TrainAlgorithmResponse> = new EventEmitter();

  constructor(ngxLogger: NGXLogger) {
    this.logger = new MagiunLogger(ModelService.name, ngxLogger)
  }

  modelArrived(model: TrainAlgorithmResponse): void {
    this.models.push(model);
    this.change.emit(model);
  }
}
