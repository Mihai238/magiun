import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ChartData} from '../../../../model/chart-data';

@Component({
  selector: 'chart-bar-settings',
  templateUrl: './bar-settings.component.html',
  styleUrls: ['./bar-settings.component.css']
})
export class BarSettingsComponent implements OnInit {

  @Output() settingsUpdated = new EventEmitter();

  constructor() { }

  ngOnInit() {
    const data = [{
      x: ['true', 'fase'],
      y: [14, 29],
      type: 'bar'
    }];

    const layout = {
      title: 'Bar chart title'
    };

    const chartData: ChartData = {
      data: data,
      layout: layout
    };

    this.settingsUpdated.emit(chartData);
  }

}
