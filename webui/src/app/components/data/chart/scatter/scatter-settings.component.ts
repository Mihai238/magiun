import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DataSet} from '../../../../model/data-set';
import {DataService} from '../../../../services/data.service';
import {ChartData} from '../../../../model/chart-data';

@Component({
  selector: 'chart-scatter-settings',
  templateUrl: './scatter-settings.component.html',
  styleUrls: ['./scatter-settings.component.css']
})
export class ScatterSettingsComponent implements OnInit {

  @Input() dataSet: DataSet;
  @Output() settingsUpdated = new EventEmitter();

  constructor(private dataService: DataService) { }

  ngOnInit() {
    const data = [{
      x: [1, 2, 3, 4],
      y: [10, 15, 13, 17],
      mode: 'markers',
      type: 'scatter'
    }];

    const layout = {
      title: 'Scatter plot title'
    };

    const chartData: ChartData = {
      data: data,
      layout: layout
    };

    this.settingsUpdated.emit(chartData);
  }

}
