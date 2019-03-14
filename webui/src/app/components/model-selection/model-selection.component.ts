import {Component} from '@angular/core';
import {NGXLogger} from "ngx-logger";
import {DataService} from "../../services/data.service";
import {NavigationEnd, Router} from "@angular/router";
import {Column, DataSet} from "../../model/data-set.model";
import {MagiunLogger} from "../../util/magiun.logger";
import {Observable} from "rxjs";
import {of} from "rxjs/observable/of";
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

@Component({
  selector: 'app-model-selection',
  templateUrl: './model-selection.component.html',
  styleUrls: ['./model-selection.component.scss']
})
export class ModelSelectionComponent {

  private logger: MagiunLogger;
  private datasets: DataSet[] = [];
  private selectedDataset: DataSet;
  private explanatoryVariables: Column[] = [];
  private possibleExplanatoryVariables: Column[] = [];
  private targetVariable: Column;
  private goal: string = "regression";
  private tradeOff: string = "";

  constructor(
    private dataService: DataService,
    private recommenderService: RecommenderRestService,
    private router: Router,
    private translate: TranslateService,
    private dialogService: DialogService,
    ngxLogger: NGXLogger
  ) {
    this.logger = new MagiunLogger(ModelSelectionComponent.name, ngxLogger);
    this.logger.info("created!");
    this.router.events.subscribe((val) => {
      if (val instanceof NavigationEnd && val.url == '/model-selection') {
        this.refreshTheDatasets();
        this.getDistributions();
      }
    })
  }

  private refreshTheDatasets(): void {
    this.logger.info("view selected!");
    this.logger.info("trying to retrieve current data!");
    this.dataService.getDataSets().subscribe(value => {
      this.datasets = value;
      this.refreshSelectedDataset();
    });
  }

  // todo: implement me
  private getDistributions(): void {

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

  updateSelectedDataset(event: any): void {
    this.selectedDataset = this.datasets[<number>event];
    this.targetVariable = this.selectedDataset.schema.columns[0];
    this.explanatoryVariables = [];
    this.logSelectedDatasetInfo();
  }

  updateTargetVariable(event: any): void {
    this.targetVariable = this.selectedDataset.schema.columns[<number>event];
    this.possibleExplanatoryVariables = CollectionsUtils.withoutElement(this.selectedDataset.schema.columns, this.targetVariable);
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
      .subscribe(() => {});
  }

  //TODO: implement me
  showDistributions(): void {
    if (this.explanatoryVariables.length == 0) {
      alert(this.translate.instant("MODEL_SELECTION.NO_VARIABLES_SELECTED"));
      return;
    }

    this.dialogService.addDialog(DistributionsModalComponent, { dataset: this.selectedDataset, columns: [this.targetVariable, ...this.explanatoryVariables]})
      .subscribe((result) => {
        console.log(result)
      });
  }

  // todo: adapt me
  private createRecommenderRequest(): RecommenderRequest {
    return new RecommenderRequest(
      this.selectedDataset.id,
      this.goal,
      this.tradeOff,
      this.targetVariable.index,
      this.explanatoryVariables.map(c => c.index),
      Distribution.BERNOULLI_DISTRIBUTION,
      []
    )
  }
}
