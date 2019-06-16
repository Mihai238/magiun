import {Component} from '@angular/core';
import {DialogComponent, DialogService} from 'ng2-bootstrap-modal';
import {TranslateService} from "@ngx-translate/core";

export interface DistributionsModal {
  algorithm: string;
}

@Component({
  selector: 'app-recommendation-info-modal',
  templateUrl: './recommendation-explanation-modal.component.html',
  styleUrls: ['./recommendation-explanation-modal.component.scss']
})
export class RecommendationExplanationModalComponent extends DialogComponent<DistributionsModal, string> implements DistributionsModal {

  algorithm: string;

  constructor(dialogService: DialogService, private translate: TranslateService) {
    super(dialogService);
  }
}
