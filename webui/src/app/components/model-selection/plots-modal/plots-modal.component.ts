import {AfterViewInit, Component} from "@angular/core";
import {Column} from "../../../model/data-set.model";
import {DialogComponent, DialogService} from 'ng2-bootstrap-modal'

declare var Plotly: any;

export interface PlotsModal {
  column: Column
  data: number[]
}

@Component({
  selector: 'app-plots-modal',
  templateUrl: './plots-modal.component.html',
  styleUrls: ['./plots-modal.component.scss']
})
export class PlotsModalComponent extends DialogComponent<PlotsModal, [Column, number[]]> implements PlotsModal, AfterViewInit {

  column: Column;
  data: number[];

  constructor(dialogService: DialogService) {
    super(dialogService);
  }

  ngAfterViewInit(): void {
    this.plotHistogram();
    this.plotBoxPlot();
  }

  private plotHistogram(): void {
    const trace = {
      x: this.data,
      type: 'histogram',
    };

    const plotData = [trace];

    Plotly.newPlot('histogram', plotData, {height: 400, width: 500, bargap: 0.05, xaxis: {title: this.column.name}, yaxis: {title: "Count"}})
  }

  private plotBoxPlot(): void {
    const trace = {
      y: this.data,
      type: 'box',
      name: this.column.name
    };

    const plotData = [trace];

    Plotly.newPlot('boxPlot', plotData, {height: 400, width: 500})
  }




}
