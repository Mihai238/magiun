import {Component} from '@angular/core';
import {NGXLogger} from "ngx-logger";
import {DataService} from "../../services/data.service";
import {Data, NavigationEnd, Router} from "@angular/router";
import {DataSet} from "../../model/data-set.model";

@Component({
  selector: 'app-model-selection',
  templateUrl: './model-selection.component.html',
  styleUrls: ['./model-selection.component.scss']
})
export class ModelSelectionComponent {

  private datasets: DataSet[] = [];
  private selectedDataset: DataSet;

  constructor(
    private dataService: DataService,
    private logger: NGXLogger,
    private router: Router
  ) {
    this.logger.info("ModelSelectionComponent: created!");
    this.router.events.subscribe((val) => {
      if (val instanceof NavigationEnd && val.url == '/model-selection') {
        this.updateTheDatasets();
      }
    })
  }

  private updateTheDatasets(): void {
    this.logger.info("ModelSelectionComponent: view selected!");
    this.logger.info("ModelSelectionComponent: trying to retrieve data!");
    this.dataService.getDataSets().subscribe(value => {
      this.datasets = value;
      this.updatedSelectedDataset();
    });
  }

  private updatedSelectedDataset(): void {
    let numberOfDatasets = this.datasets.length;

    if (this.selectedDataset == null && numberOfDatasets > 0) {
      this.selectedDataset = this.datasets[0];
    } else if (this.selectedDataset != null && numberOfDatasets > 0 && !this.datasets.some(d => this.compareDatasets(this.selectedDataset, d))) {
      this.selectedDataset = this.datasets[0];
      this.logger.info("HOLA!")
    } else if (this.selectedDataset != null && numberOfDatasets == 0) {
      this.selectedDataset = null;
    }
  }

  private compareDatasets(d1: DataSet, d2: DataSet): boolean {
    return d1 != null && d2 != null && d1.name == d2.name && d1.id == d2.id && d1.schema.totalCount == d2.schema.totalCount;
  }
}
