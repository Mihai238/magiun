import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})
export class ChartComponent implements OnInit {

  public ChartType = ChartType;
  selectedChartType: ChartType;
  chartData: ChartData;

  constructor() {
  }

  ngOnInit() {
    this.chartData = {xValues: [1, 2, 3, 0, 9, 12], yValues: [1, 2, 4, 2, 2, 1]};
    this.selectedChartType = ChartType.histogram;
  }

  chartTypes(): Array<string> {
    return Object.keys(this.ChartType);
  }

  onSelectChart(chartTypeString: string) {
    this.selectedChartType = ChartType[chartTypeString];
  }

}

export class ChartData {
  xValues: any[];
  yValues: any[];
}

export enum ChartType {
  histogram = 'Histogram',
  bar = 'Bar'
}
