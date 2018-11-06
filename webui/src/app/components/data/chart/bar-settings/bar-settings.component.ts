import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DataService} from "../../../../services/data.service";
import {DataSet} from "../../../../model/data-set.model";
import {ChartData} from "../../../../model/chart-data.model";

@Component({
  selector: 'chart-bar-settings',
  templateUrl: './bar-settings.component.html',
  styleUrls: ['./bar-settings.component.scss']
})
export class BarSettingsComponent implements OnInit {

  @Input() dataSet: DataSet;
  @Output() settingsUpdated = new EventEmitter<ChartData>();

  constructor(private dataService: DataService) {
  }

  ngOnInit() {
    this.update();
  }

  private update(): void {
    const data = [{
      x: ['giraffes', 'orangutans', 'monkeys'],
      y: [20, 14, 23],
      type: 'bar'
    }];

    const layout = {};

    const chartData = {
      data: data,
      layout: layout
    };

    this.settingsUpdated.emit(chartData);
  }

}
