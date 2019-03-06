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

@Component({
  selector: 'app-model-selection',
  templateUrl: './model-selection.component.html',
  styleUrls: ['./model-selection.component.scss']
})
export class ModelSelectionComponent {

  private logger: MagiunLogger;
  private datasets: DataSet[] = [];
  private selectedDataset: DataSet;
  private columnsToIgnore: Column[] = [];
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
      this.columnsToIgnore = [];
    }
    this.logSelectedDatasetInfo();
  }

  updateSelectedDataset(event: any): void {
    this.selectedDataset = this.datasets[<number>event];
    this.targetVariable = this.selectedDataset.schema.columns[0];
    this.columnsToIgnore = [];
    this.logSelectedDatasetInfo();
  }

  updateTargetVariable(event: any): void {
    this.targetVariable = this.selectedDataset.schema.columns[<number>event];
    this.logTargetVariable();
  }

  private logSelectedDatasetInfo(): void {
    if (this.selectedDataset != null) {
      this.logger.info("the selected dataset is \"" + this.selectedDataset.name + "\"");
    } else {
      this.logger.info("the selected dataset is null");
    }
    this.logTargetVariable();
  }

  private logTargetVariable(): void {
    if (this.targetVariable != null) {
      this.logger.info("the selected target variable is \"" + this.targetVariable.name + "\"");
    } else {
      this.logger.info("the selected target variable is null");
    }
  }

  private refresh(): void {
    this.selectedDataset = this.datasets[0];
    this.targetVariable = this.selectedDataset.schema.columns[0];
    this.columnsToIgnore = [];
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
      this.columnsToIgnore.push(<Column>column);
    } else {
      this.columnsToIgnore = CollectionsUtils.deleteEntryFromArray(this.columnsToIgnore, <Column>column);
    }
    this.logger.info(`${(checked) ? "adding" : "removing"} column "${column.name}" ${(checked) ? "to" : "from"} the ignore list`);
  }

  recommend(): void {
    if (this.columnsToIgnore.some(c => this.targetVariable.equals(c))) {
      alert(this.translate.instant("MODEL_SELECTION.TARGET_VARIABLE_IS_IGNORE"));
    } else if (this.columnsToIgnore.length > 0 && this.columnsToIgnore.length == this.selectedDataset.schema.columns.length - 1) {
      alert(this.translate.instant("MODEL_SELECTION.NO_VARIABLES_LEFT"));
    }

    this.recommenderService
      .recommend(this.createRecommenderRequest())
      .subscribe(a => {});

    this.logger.warn("hola!");
  }

  //TODO: implement me
  showDistributions(): void {
    this.dialogService.addDialog(DistributionsModalComponent, { dataset: this.selectedDataset })
      .subscribe((result) => {
        console.log(result)
      });
  }

  private createRecommenderRequest(): RecommenderRequest {
    return new RecommenderRequest(
      this.selectedDataset.id,
      this.goal,
      this.tradeOff,
      this.targetVariable.index,
      this.columnsToIgnore.map(c => c.index)
    )
  }
}
