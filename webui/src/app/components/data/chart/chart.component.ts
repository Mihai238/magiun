import {Component, ElementRef, Input, OnChanges, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import {ChartData} from '../../../model/chart-data.model';
import {DataSet} from '../../../model/data-set.model';
import {NGXLogger} from 'ngx-logger';
import {MagiunLogger} from "../../../util/magiun.logger";

declare var Plotly: any;

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.scss']
})
export class ChartComponent implements OnInit, OnChanges {

  private logger: MagiunLogger;

  @Input() dataSet: DataSet;
  @ViewChild('chart') chartEl: ElementRef;

  public ChartType = ChartType;
  selectedChartType: ChartType;

  constructor(ngxlogger: NGXLogger) {
    this.logger = new MagiunLogger(ChartComponent.name, ngxlogger);
  }

  ngOnInit() {
    this.selectedChartType = ChartType.histogram;
    this.logger.debug('DataSet: ' +  this.dataSet);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.ngOnInit();
  }

  chartTypes(): Array<string> {
    return Object.keys(this.ChartType);
  }

  onSelectChart(chartTypeString: string) {
    this.selectedChartType = ChartType[chartTypeString];
  }

  handleSettingsUpdated(chartData: ChartData) {
    this.logger.info('settings updated');

    chartData.layout.height = 400;
    chartData.layout.width = 500;

    Plotly.newPlot(this.chartEl.nativeElement, chartData.data, chartData.layout, {displayModeBar: false});
  }

}

enum ChartType {
  histogram = 'Histogram',
  pie = 'Pie',
  scatter = 'Scatter',
  bar = 'Bar'
}
