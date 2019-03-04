import {Component} from '@angular/core';
import {DialogComponent, DialogService} from 'ng2-bootstrap-modal';

export interface DistributionsModal {

}

@Component({
  selector: 'app-distributions-modal',
  templateUrl: './distributions-modal.component.html',
  styleUrls: ['./distributions-modal.component.scss']
})
export class DistributionsModalComponent extends DialogComponent<DistributionsModal, any> implements DistributionsModal {

  constructor(dialogService: DialogService) {
    super(dialogService);
  }

  confirm() {
    this.close();
  }
}
