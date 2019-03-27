import {Component} from '@angular/core';
import {DialogComponent, DialogService} from 'ng2-bootstrap-modal';
import {Column, DataSet} from "../../../model/data-set.model";
import {Distribution} from "../../../model/statistics/distribution.type.model";
import {PlotsModalComponent} from "../plots-modal/plots-modal.component";
import {DataService} from "../../../services/data.service";
import {TranslateService} from "@ngx-translate/core";
import {CollectionsUtils} from "../../../util/collections.utils";

export interface DistributionsModal {
  dataset: DataSet
  columns: Column[]
}

@Component({
  selector: 'app-distributions-modal',
  templateUrl: './distributions-modal.component.html',
  styleUrls: ['./distributions-modal.component.scss']
})
export class DistributionsModalComponent extends DialogComponent<DistributionsModal, [DataSet, Column[]]> implements DistributionsModal {

  Distribution = Distribution;
  dataset: DataSet;
  columns: Column[];

  constructor(dialogService: DialogService, private dataService: DataService, private translate: TranslateService) {
    super(dialogService);
  }

  confirm() {
    if (!this.validateDistributions()) {
      alert(this.translate.instant('MODEL_SELECTION.DISTRIBUTIONS_MODAL.NOT_ALL_DISTRIBUTIONS'));
      return;
    }

    this.result = [this.dataset, this.columns];
    this.close();
  }

  distributionSelected(c: Column, d: string) {
    this.columns.filter(column => column.equals(c)).forEach(column => column.distribution = Distribution.value(d));
  }

  plot(c: Column) {
    this.dataService.getRandomSample(this.dataset,[c.name]).subscribe(
      rows => {
        this.dialogService.addDialog(
          PlotsModalComponent,
          {
            column: c,
            data: CollectionsUtils.cleanArray(rows.map(row => parseFloat(row.values.pop())))
          }
        )
          .subscribe()
      });
  }

  private validateDistributions(): boolean {
    return this.columns.filter(c => c.distribution == null || c.distribution == Distribution.UNKNOWN).length == 0
  }
}
