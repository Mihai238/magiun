import {Component} from '@angular/core';
import {DialogComponent, DialogService} from 'ng2-bootstrap-modal';
import {Column, DataSet} from "../../../model/data-set.model";
import {Distribution} from "../../../model/statistics/distribution.type.model";
import {PlotsModalComponent} from "../plots-modal/plots-modal.component";
import {DataService} from "../../../services/data.service";

export interface DistributionsModal {
  dataset: DataSet
}

@Component({
  selector: 'app-distributions-modal',
  templateUrl: './distributions-modal.component.html',
  styleUrls: ['./distributions-modal.component.scss']
})
export class DistributionsModalComponent extends DialogComponent<DistributionsModal, DataSet> implements DistributionsModal {

  Distribution = Distribution;
  dataset: DataSet;

  constructor(dialogService: DialogService, private dataService: DataService) {
    super(dialogService);
  }

  confirm() {
    this.close();
  }

  plot(c: Column) {
    this.dataService.getDataSample(this.dataset, [c.name]).subscribe(
      rows => {
        this.dialogService.addDialog(PlotsModalComponent, { column: c, data: rows.map(row => parseFloat(row.values[c.index]))}).subscribe()
      });

  }
}
