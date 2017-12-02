import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ChartData} from '../../../../model/chart-data';
import {DataService} from '../../../../services/data.service';
import {DataSet} from '../../../../model/data-set';

@Component({
  selector: 'chart-pie-settings',
  templateUrl: './pie-settings.component.html'
})
export class PieSettingsComponent implements OnInit {

  @Input() dataSet: DataSet;
  @Output() settingsUpdated = new EventEmitter();

  constructor(private dataService: DataService) {
  }

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
