import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ChartData} from '../../../../model/chart-data';
import {DataService} from '../../../../services/data.service';
import {Column, DataSet} from '../../../../model/data-set';

@Component({
  selector: 'chart-pie-settings',
  templateUrl: './pie-settings.component.html'
})
export class PieSettingsComponent implements OnInit {

  @Input() dataSet: DataSet;
  @Output() settingsUpdated = new EventEmitter();

  selectedColumn: Column;

  constructor(private dataService: DataService) {
  }

  ngOnInit(): void {
  }

  onUpdateFirstColumn(column: Column) {
    this.selectedColumn = column;
    this.getDataAndUpdate();
  }

  private getDataAndUpdate() {
    this.dataService.getAllData(this.dataSet)
      .subscribe(dataRows => {
        const values = dataRows.map(row => row.values[this.selectedColumn.index]);
        this.update(values);
      });
  }

  private update(values: any[]) {
    const data = [{
      values: values,
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
