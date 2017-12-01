import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ChartData} from '../../../model/chart-data';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})
export class ChartComponent implements OnInit {

  @ViewChild('chart') chartEl: ElementRef;

  public ChartType = ChartType;
  selectedChartType: ChartType;

  constructor() {
  }

  ngOnInit() {
    this.selectedChartType = ChartType.histogram;
  }

  chartTypes(): Array<string> {
    return Object.keys(this.ChartType);
  }

  onSelectChart(chartTypeString: string) {
    this.selectedChartType = ChartType[chartTypeString];
  }

  handleSettingsUpdated(chartData: ChartData) {
    chartData.layout.height = 400;
    chartData.layout.width = 500;

    Plotly.newPlot(this.chartEl.nativeElement, chartData.data, chartData.layout, {displayModeBar: false});
  }

}

enum ChartType {
  histogram = 'Histogram',
  bar = 'Bar',
  pie = 'Pie'
}
