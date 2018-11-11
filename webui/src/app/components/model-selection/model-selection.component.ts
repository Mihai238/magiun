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

  constructor(
    private dataService: DataService,
    ngxlogger: NGXLogger,
    private router: Router
  ) {
    this.logger = new MagiunLogger(ModelSelectionComponent.name, ngxlogger);
    this.logger.info("created!");
    this.router.events.subscribe((val) => {
      if (val instanceof NavigationEnd && val.url == '/model-selection') {
        this.refreshTheDatasets();
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

  private refreshSelectedDataset(): void {
    let numberOfDatasets = this.datasets.length;

    if (this.selectedDataset == null && numberOfDatasets > 0) {
      this.selectedDataset = this.datasets[0];
      this.columnsToIgnore = [];
    } else if (this.selectedDataset != null && numberOfDatasets > 0 && !this.datasets.some(d => this.selectedDataset.equals(d))) {
      this.selectedDataset = this.datasets[0];
      this.columnsToIgnore = [];
    } else if (this.selectedDataset != null && numberOfDatasets == 0) {
      this.selectedDataset = null;
      this.columnsToIgnore = [];
    }
    this.logSelectedDatasetInfo();
  }

  updateSelectedDataset(event: any): void {
    this.selectedDataset = this.datasets[<number>event];
    this.logSelectedDatasetInfo();
  }

  private logSelectedDatasetInfo() {
    if (this.selectedDataset != null) {
      this.logger.info("the selected dataset is \"" + this.selectedDataset.name + "\"!");
    } else {
      this.logger.info("the selected dataset is null!")
    }
  }

  addIgnoreColumn(column: any) {
    this.columnsToIgnore.push(<Column>column);
    this.logger.info("adding column \"" + column.name + "\" to the ignore list");
  }

  removeIgnoreColumn(column: any) {
    this.columnsToIgnore = CollectionsUtils.deleteEntryFromArray(this.columnsToIgnore, <Column>column);
    this.logger.info("removing column \"" + column.name + "\" from the ignore list");
  }

  columns = (text: string): Observable<Column[]> => {
    if (this.selectedDataset != null) {
      return of(this.selectedDataset.schema.columns)
    } else {
      return of()
    }
  }
}
