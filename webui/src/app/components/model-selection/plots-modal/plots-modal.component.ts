import {AfterViewInit, Component} from "@angular/core";
import {Column} from "../../../model/data-set.model";
import {DialogComponent, DialogService} from 'ng2-bootstrap-modal'
import {NormalDistribution} from "../../../model/statistics/NormalDistribution";

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
    this.qqPlot();
  }

  private plotHistogram(): void {
    const trace = {
      x: this.data,
      type: 'histogram',
    };

    const plotData = [trace];

    Plotly.newPlot('histogram', plotData, {
      height: 400,
      width: 500,
      bargap: 0.05,
      xaxis: {title: this.column.name},
      yaxis: {title: "Count"},
      title: "Histogram"
    })
  }

  private plotBoxPlot(): void {
    const trace = {
      y: this.data,
      type: 'box',
      name: this.column.name
    };

    const plotData = [trace];

    Plotly.newPlot('boxPlot', plotData, {height: 400, width: 500, title: "Boxplot "})
  }

  private qqPlot(): void {
    let n = new NormalDistribution(0 ,1).sample(this.data.length).sort((n1, n2) => n2 - n1);

    console.log(n);

    const trace = {
      x: n, // todo
      y: this.data.sort((n1, n2) => n2 - n1),
      type: 'scatter',
      mode: "markers",
      name: this.column.name,
      marker: {
        size: 8
      }
    };

    const maxY = Math.max.apply(Math, this.data);
    const minY = Math.min.apply(Math, this.data);
    const maxX = Math.max.apply(Math, n);
    const minX = Math.min.apply(Math, n);

    const straightLine = {
      x0: minX * 0.9,
      y0: minY * 0.9,
      x1: maxX * 1.1,
      y1: maxY * 1.1,
      type: "line",
      line: {
        color: 'rgb(139,0,0)',
        width: 5
      }
    };

    const plotData = [trace];

    Plotly.newPlot('qqPlot', plotData, {
      height: 400,
      width: 500,
      title: "Normal Q-Q Plot",
      xaxis: {
        showgrid: true,
        showline: true,
        title: 'Theoretical Quantiles'
      },
      yaxis: {
        nticks: 11,
        showgrid: true,
        showline: true,
        title: "Sample Quantiles"
      },
      shapes: [straightLine]
    })
  }


}
