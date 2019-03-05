import {Component} from "@angular/core";
import {Column} from "../../../model/data-set.model";
import {DialogComponent, DialogService} from 'ng2-bootstrap-modal'

export interface PlotsModal {
  column: Column
}

@Component({
  selector: 'app-plots-modal',
  templateUrl: './plots-modal.component.html',
  styleUrls: ['./plots-modal.component.scss']
})
export class PlotsModalComponent extends DialogComponent<PlotsModal, Column> implements PlotsModal {

  column: Column;

  constructor(dialogService: DialogService) {
    super(dialogService);
  }



}
