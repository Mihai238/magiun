import {TrainAlgorithmResponse} from "../../../model/response/train.algorithm.response.model";
import {Input} from "@angular/core";
import {AlgorithmImplementation} from "../../../model/algorithm/algorithm.implementation.model";
import {DialogService} from "ng2-bootstrap-modal";
import {RegressionPlotsModalComponent} from "../plots/regression-plots-modal/regression-plots-modal.component";
import {StatisticsUtils} from "../../../util/statistics.utils";

export abstract class SummaryComponent {
  @Input() model: TrainAlgorithmResponse;

  Math = Math;
  AlgorithmImplementation = AlgorithmImplementation;
  StatisticsUtils = StatisticsUtils;
  numberFormat: string = '1.2-3';

  protected constructor(protected dialogService: DialogService) {}

  plot(): void {
    this.dialogService.addDialog(
      RegressionPlotsModalComponent,
      {
        residuals: this.model.residuals,
        fittedValues: this.model.fittedValues,
        dataSample: this.model.dataSample
      }
    ).subscribe();
  }
}
