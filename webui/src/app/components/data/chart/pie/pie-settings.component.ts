import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {ChartData} from '../../../../model/chart-data';

@Component({
  selector: 'chart-pie-settings',
  templateUrl: './pie-settings.component.html'
})
export class PieSettingsComponent implements OnInit {

  @Output() settingsUpdated = new EventEmitter();

  ngOnInit(): void {
    const data = [{
      values: [4, 5, 10],
      type: 'pie'
    }];

    const layout = {
      title: 'Pie chart title'
    };

    const chartData: ChartData = {
      data: data,
      layout: layout
    };

    this.settingsUpdated.emit(chartData);
  }

}
