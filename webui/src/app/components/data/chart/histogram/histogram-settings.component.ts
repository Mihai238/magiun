import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {ChartData} from '../../../../model/chart-data.model';
import {Column, DataSet} from '../../../../model/data-set.model';
import {DataService} from '../../../../services/data.service';
import {NGXLogger} from "ngx-logger";

@Component({
  selector: 'chart-histogram-settings',
  templateUrl: './histogram-settings.component.html',
  styleUrls: ['./histogram-settings.component.scss']
})
export class HistogramSettingsComponent implements OnInit, OnChanges {

  @Input() dataSet: DataSet;
  @Output() settingsUpdated = new EventEmitter();

  public HistNorm = HistNorm;

  selectedColumn: Column;
  selectedHistNorm: HistNorm;
  isCumulativeEnabled: boolean;

  constructor(private dataService: DataService,
              private logger: NGXLogger) {
  }

  ngOnInit() {
    this.selectedHistNorm = HistNorm.default;
    this.isCumulativeEnabled = false;
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.ngOnInit();
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
    this.logger.info('HistogramSettingsComponent: get data and update');

    this.dataService.getAllData(this.dataSet, new Set([this.selectedColumn.name]))
      .subscribe(dataRows => {
        const values = dataRows.map(row => row.values[0]);
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
      title: `Histogram "${this.selectedColumn.name}" feature`,
      xaxis: {
        title: this.selectedColumn.name,
      },
      yaxis: {
        title: this.selectedHistNorm.labelText
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
  static default = new HistNorm('', 'default', 'occurrences');
  static percent = new HistNorm('percent', 'percent', 'percentage');
  static density = new HistNorm('density', 'density', 'density');

  static values(): HistNorm[] {
    return [this.default, this.percent, this.density];
  }

  private constructor(public value: string, public optionText: string, public labelText: String) {
  }

  toString() {
    return this.optionText;
  }
}
