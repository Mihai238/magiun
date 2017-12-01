import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ChartData} from '../../../../model/chart-data';

@Component({
  selector: 'chart-histogram-settings',
  templateUrl: './histogram-settings.component.html',
  styleUrls: ['./histogram-settings.component.css']
})
export class HistogramSettingsComponent implements OnInit {

  @Output() settingsUpdated = new EventEmitter();

  constructor() { }

  ngOnInit() {
    const data = [{
      x: [1, 2, 3, 0, 9, 12],
      type: 'histogram'
    }];

    const layout = {
      title: 'Histogram title',
      xaxis: {
        title: 'label X',
      },
      yaxis: {
        title: 'label Y'
      }
    };

    const chartData: ChartData = {
      data: data,
      layout: layout
    };

    this.settingsUpdated.emit(chartData);
  }

}
