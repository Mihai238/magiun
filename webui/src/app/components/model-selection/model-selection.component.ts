import {Component, Inject} from '@angular/core';
import {NGXLogger} from "ngx-logger";
import {DataService} from "../../services/data.service";
import {NavigationEnd, Router} from "@angular/router";
import {Column, DataSet} from "../../model/data-set.model";
import {MagiunLogger} from "../../util/magiun.logger";
import 'rxjs/add/operator/filter';
import 'rxjs/add/operator/debounceTime';
import 'rxjs/add/operator/map';
import 'rxjs/add/operator/first';
import {CollectionsUtils} from "../../util/collections.utils";
import {TranslateService} from "@ngx-translate/core";
import {RecommenderRestService} from "../../rest/recommender.rest.service";
import {RecommenderRequest} from "../../model/recommender-request.model";
import {DialogService} from 'ng2-bootstrap-modal';
import {DistributionsModalComponent} from "./distributions-modal/distributions-modal.component";
import {Distribution} from "../../model/statistics/distribution.type.model";
import {Algorithm} from "../../model/algorithm/algorithm.model";
import {DOCUMENT} from "@angular/common";

@Component({
  selector: 'app-model-selection',
  templateUrl: './model-selection.component.html',
  styleUrls: ['./model-selection.component.scss']
})
export class ModelSelectionComponent {

  private logger: MagiunLogger;
  Number = Number;
  datasets: DataSet[] = [];
  selectedDataset: DataSet;
  explanatoryVariables: Column[] = [];
  possibleExplanatoryVariables: Column[] = [];
  targetVariable: Column;
  goal: string = "GoalRegression";
  tradeOff: string = "";
  definedDistributions: Distribution[] = [];
  checkedDistributions: boolean = false;
  algorithmRecommendations: Algorithm[] = [];

  constructor(
    private dataService: DataService,
    private recommenderService: RecommenderRestService,
    private router: Router,
    private translate: TranslateService,
    private dialogService: DialogService,
    ngxLogger: NGXLogger,
    @Inject(DOCUMENT) document
  ) {
    this.logger = new MagiunLogger(ModelSelectionComponent.name, ngxLogger);
    this.router.events.subscribe((val) => {
      if (val instanceof NavigationEnd && val.url == '/model-selection') {
        this.refreshTheDatasets();
      }
    });
    this.logger.info("created!");
  }

  private refreshTheDatasets(): void {
    this.logger.info("view selected!");
    this.logger.info("trying to retrieve current data!");
    this.dataService.getDataSets().subscribe(value => {
      this.datasets = value;
      this.refreshSelectedDataset();
      this.getDistributions();
    });
  }

  private getDistributions(): void {
    this.dataService.getDistributions(this.selectedDataset)
      .subscribe((result) => {
        for (const key of Object.keys(result)) {
          this.selectedDataset.schema.columns
            .filter(c => c.name == key)
            .forEach(c => {
              console.log(result[key]);
              c.distribution = result[key];
            })
        }
      })
  }

  private refreshSelectedDataset(): void {
    let numberOfDatasets = this.datasets.length;

    if (this.selectedDataset == null && numberOfDatasets > 0) {
      this.refresh();
    } else if (this.selectedDataset != null && numberOfDatasets > 0 && !this.datasets.some(d => this.selectedDataset.equals(d))) {
      this.refresh();
    } else if (this.selectedDataset != null && numberOfDatasets == 0) {
      this.selectedDataset = null;
      this.targetVariable = null;
      this.explanatoryVariables = [];
    }
    this.logSelectedDatasetInfo();
  }

  updateSelectedDataset(event: number): void {
    if (event == null || event < 0) {
      this.logger.error(`index should be >= 0 but got ${event}`);
      return;
    }

    this.selectedDataset = this.datasets[event];
    this.targetVariable = this.selectedDataset.schema.columns[0];
    this.explanatoryVariables = [];
    this.possibleExplanatoryVariables = CollectionsUtils.withoutElement(this.selectedDataset.schema.columns, this.targetVariable);
    this.getDistributions();
    this.logSelectedDatasetInfo();
  }

  updateTargetVariable(event: number): void {
    if (event == null || event < 0) {
      this.logger.error(`index should be >= 0 but got ${event}`);
      return;
    }

    this.targetVariable = this.selectedDataset.schema.columns[event];
    this.possibleExplanatoryVariables = CollectionsUtils.withoutElement(this.selectedDataset.schema.columns, this.targetVariable);
    this.explanatoryVariables = CollectionsUtils.withoutElement(this.explanatoryVariables, this.targetVariable);
    this.logTargetVariable();
  }

  private logSelectedDatasetInfo(): void {
    if (this.selectedDataset != null) {
      this.logger.info(`the selected dataset is "${this.selectedDataset.name}"`);
    } else {
      this.logger.info("the selected dataset is null");
    }
    this.logTargetVariable();
  }

  private logTargetVariable(): void {
    if (this.targetVariable != null) {
      this.logger.info(`the selected target variable is "${this.targetVariable.name}"`);
    } else {
      this.logger.info("the selected target variable is null");
    }
  }

  private refresh(): void {
    this.selectedDataset = this.datasets[0];
    this.targetVariable = this.selectedDataset.schema.columns[0];
    this.explanatoryVariables = [];
    this.possibleExplanatoryVariables = CollectionsUtils.withoutElement(this.selectedDataset.schema.columns, this.targetVariable)
  }

  updateGoal(goal: any) {
    this.goal = <string>goal;
    this.logger.info(`the selected goal is "${this.goal}"`);
  }

  updateTradeOff(tradeOff: any) {
    this.tradeOff = <string>tradeOff;
    this.logger.info(`the selected trade-off is "${this.tradeOff}"`);
  }

  addCheckBoxChange(column: any, checked: boolean): void {
    if (checked) {
      this.explanatoryVariables.push(<Column>column);
    } else {
      this.explanatoryVariables = CollectionsUtils.deleteEntryFromArray(this.explanatoryVariables, <Column>column);
    }
    this.logger.info(`${(checked) ? "adding" : "removing"} column "${column.name}" ${(checked) ? "to" : "from"} the explanatory variable list`);
  }

  recommend(): void {
    if (this.explanatoryVariables.length == 0) {
      alert(this.translate.instant("MODEL_SELECTION.NO_VARIABLES_SELECTED"));
      return;
    }

    this.recommenderService
      .recommend(this.createRecommenderRequest())
      .subscribe((result) => {
        this.algorithmRecommendations = result;

        if (result.length == 0 && this.goal == "GoalClassification" && !Distribution.isDiscrete(this.definedDistributions[0])) {
          alert(this.translate.instant("MODEL_SELECTION.EMPTY_CLASSIFICATION_RECOMMENDATIONS"))
        }
      });
  }

  showDistributions(): void {
    if (this.explanatoryVariables.length == 0) {
      alert(this.translate.instant("MODEL_SELECTION.NO_VARIABLES_SELECTED"));
      return;
    }

    this.dialogService.addDialog(DistributionsModalComponent, { dataset: this.selectedDataset, columns: [this.targetVariable, ...this.explanatoryVariables]})
      .subscribe((result) => {
        if (result != undefined) {
          this.definedDistributions = result[1].map(c => c.distribution);
          this.checkedDistributions = true;
        }
      });
  }

  private createRecommenderRequest(): RecommenderRequest {
    if (!this.checkedDistributions) {
      alert(this.translate.instant("MODEL_SELECTION.CHECK_THE_DISTRIBUTIONS"));
      return;
    }

    if (!this.validateExplanatoryVariablesDistributions()) {
      alert(this.translate.instant("MODEL_SELECTION.UNKNOWN_DISTRIBUTIONS"));
      this.checkedDistributions = false;
      return;
    }

    if (Distribution.isNullOrUnknown(this.targetVariable.distribution)) {
      alert(this.translate.instant("MODEL_SELECTION.TARGET_VARIABLE_UNKNOWN_DISTRIBUTION"));
      this.checkedDistributions = false;
      return;
    }

    return new RecommenderRequest(
      this.selectedDataset.id,
      this.goal,
      this.tradeOff,
      this.targetVariable.index,
      this.explanatoryVariables.map(c => c.index),
      this.definedDistributions[0],
      this.definedDistributions.slice(1)
    );
  }

  private validateExplanatoryVariablesDistributions(): boolean {
    return this.explanatoryVariables.map(c => c.distribution)
      .filter(d => Distribution.isNullOrUnknown(d)).length == 0
  }

  train(index: number): void {
    console.log("train algorithm " + index);
    this.algorithmRecommendations[index].parameters.forEach(p => {
      const element: HTMLElement = document.getElementById(p.name + "-" + index);
      p.value = (<HTMLInputElement>element).value;
    });


    // todo send train-request
  }
}
