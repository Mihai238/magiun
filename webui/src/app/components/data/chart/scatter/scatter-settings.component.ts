import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Column, DataSet} from '../../../../model/data-set.model';
import {DataService} from '../../../../services/data.service';
import {ChartData} from '../../../../model/chart-data.model';

@Component({
  selector: 'chart-scatter-settings',
  templateUrl: './scatter-settings.component.html',
  styleUrls: ['./scatter-settings.component.scss']
})
export class ScatterSettingsComponent implements OnInit {

  @Input() dataSet: DataSet;
  @Output() settingsUpdated = new EventEmitter();

  selectedFirstColumn: Column;
  selectedSecondColumn: Column;

  constructor(private dataService: DataService) { }

  ngOnInit() {
  }

  onUpdateFirstColumn(column: Column) {
    this.selectedFirstColumn = column;
    this.getDataAndUpdate();
  }

  onUpdateSecondColumn(column: Column) {
    this.selectedSecondColumn = column;
    this.getDataAndUpdate();
  }

  private getDataAndUpdate() {
    if (this.selectedFirstColumn && this.selectedSecondColumn) {
      this.dataService.getDataSample(this.dataSet, [this.selectedFirstColumn.name, this.selectedSecondColumn.name])
        .subscribe(dataRows => {
          const values1 = dataRows.map(row => row.values[0]);
          const values2 = dataRows.map(row => row.values[1]);
          this.update(values1, values2);
        });
    }
  }

  private update(x, y: any[]) {
    const data = [{
      x: x,
      y: y,
      mode: 'markers',
      type: 'scatter'
    }];

    const layout = {
      title: `Scatter plot ${this.selectedFirstColumn.name} x ${this.selectedSecondColumn.name}`,
      xaxis: {
        title: this.selectedFirstColumn.name,
      },
      yaxis: {
        title: this.selectedSecondColumn.name
      }
    };

    const chartData: ChartData = {
      data: data,
      layout: layout
    };

    this.settingsUpdated.emit(chartData);
  }

}
