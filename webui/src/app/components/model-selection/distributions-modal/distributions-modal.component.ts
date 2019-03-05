import {Component} from '@angular/core';
import {DialogComponent, DialogService} from 'ng2-bootstrap-modal';
import {Column} from "../../../model/data-set.model";
import {Distribution} from "../../../model/distribution";
import {PlotsModalComponent} from "../plots-modal/plots-modal.component";

export interface DistributionsModal {
  columns: Column[]
}

@Component({
  selector: 'app-distributions-modal',
  templateUrl: './distributions-modal.component.html',
  styleUrls: ['./distributions-modal.component.scss']
})
export class DistributionsModalComponent extends DialogComponent<DistributionsModal, Column[]> implements DistributionsModal {

  Distribution = Distribution;
  columns: Column[];

  constructor(dialogService: DialogService) {
    super(dialogService);
  }

  confirm() {
    this.close();
  }

  plot(c: Column) {
    this.dialogService.addDialog(PlotsModalComponent, { column: c }).subscribe()
  }
}
