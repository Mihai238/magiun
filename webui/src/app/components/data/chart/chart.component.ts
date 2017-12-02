import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {ChartData} from '../../../model/chart-data';
import {DataSet} from '../../../model/data-set';
import {NGXLogger} from 'ngx-logger';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})
export class ChartComponent implements OnInit {

  @Input() dataSet: DataSet;
  @ViewChild('chart') chartEl: ElementRef;

  public ChartType = ChartType;
  selectedChartType: ChartType;

  constructor(private logger: NGXLogger) {
  }

  ngOnInit() {
    this.selectedChartType = ChartType.histogram;
    this.logger.debug('DataSet: ' +  this.dataSet);
  }

  chartTypes(): Array<string> {
    return Object.keys(this.ChartType);
  }

  onSelectChart(chartTypeString: string) {
    this.selectedChartType = ChartType[chartTypeString];
  }

  handleSettingsUpdated(chartData: ChartData) {
    this.logger.info('Settings updated');

    chartData.layout.height = 400;
    chartData.layout.width = 500;

    Plotly.newPlot(this.chartEl.nativeElement, chartData.data, chartData.layout, {displayModeBar: false});
  }

}

enum ChartType {
  histogram = 'Histogram',
  pie = 'Pie',
  scatter = 'Scatter'
}
