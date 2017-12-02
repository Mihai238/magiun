import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ChartData} from '../../../../model/chart-data';
import {Column, DataSet} from '../../../../model/data-set';
import {DataService} from '../../../../services/data.service';

@Component({
  selector: 'chart-histogram-settings',
  templateUrl: './histogram-settings.component.html',
  styleUrls: ['./histogram-settings.component.css']
})
export class HistogramSettingsComponent implements OnInit {

  @Input() dataSet: DataSet;
  @Output() settingsUpdated = new EventEmitter();

  public HistNorm = HistNorm;

  selectedColumn;
  selectedHistNorm;
  isCumulativeEnabled;

  constructor(private dataService: DataService) {
  }

  ngOnInit() {
    this.selectedHistNorm = HistNorm.default;
    this.isCumulativeEnabled = false;
  }

  onUpdateColumn(column: Column) {
    this.selectedColumn = column;
    this.getDataAndUpdate();
  }

  onSelectHistNorm(histNorm: HistNorm) {
    this.selectedHistNorm = histNorm;
    this.getDataAndUpdate();
  }

  onChangeCumulative() {
    this.getDataAndUpdate();
  }

  private getDataAndUpdate() {
    this.dataService.getAllData(this.dataSet)
      .subscribe(dataRows => {
        const values = dataRows.map(e => e.values[this.selectedColumn.index]);
        this.update(values);
      });
  }

  private update(values: any[]) {
    const data = [{
      x: values,
      type: 'histogram',
      histnorm: this.selectedHistNorm.value,
      cumulative: {
        enabled: this.isCumulativeEnabled
      }
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

class HistNorm {
  static default = new HistNorm('', 'default');
  static percent = new HistNorm('percent', 'percent');
  static density = new HistNorm('density', 'density');

  static values(): HistNorm[] {
    return [this.default, this.percent, this.density];
  }

  private constructor(public value: string, public text: string) {
  }

  toString() {
    return this.text;
  }
}
