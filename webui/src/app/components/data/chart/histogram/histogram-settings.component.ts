import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {ChartData} from '../../../../model/chart-data.model';
import {Column, DataSet} from '../../../../model/data-set.model';
import {DataService} from '../../../../services/data.service';
import {NGXLogger} from "ngx-logger";
import {MagiunLogger} from "../../../../util/magiun.logger";

@Component({
  selector: 'chart-histogram-settings',
  templateUrl: './histogram-settings.component.html',
  styleUrls: ['./histogram-settings.component.scss']
})
export class HistogramSettingsComponent implements OnInit, OnChanges {

  private logger: MagiunLogger;

  @Input() dataSet: DataSet;
  @Output() settingsUpdated = new EventEmitter();

  public HistNorm = HistNorm;

  selectedColumn: Column;
  selectedGroupByColumn: Column;
  selectedHistNorm: HistNorm;
  isCumulativeEnabled: boolean;
  isOverlaidEnabled: boolean;

  constructor(private dataService: DataService,
              ngxlogger: NGXLogger) {
    this.logger = new MagiunLogger(HistogramSettingsComponent.name, ngxlogger);
  }

  ngOnInit() {
    this.selectedHistNorm = HistNorm.default;
    this.isCumulativeEnabled = false;
    this.isOverlaidEnabled = false;
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.ngOnInit();
  }

  onUpdateColumn(column: Column) {
    this.selectedColumn = column;
    this.getDataAndUpdate();
  }

  onUpdateGroupByColumn(column: Column) {
    this.selectedGroupByColumn = column;
    if (this.isOverlaidEnabled) {
      this.getDataAndUpdate();
    }
  }

  onSelectHistNorm(histNorm: HistNorm) {
    this.selectedHistNorm = histNorm;
    this.getDataAndUpdate();
  }

  onChangeOverlaid() {
    this.isOverlaidEnabled = !this.isOverlaidEnabled;
    this.getDataAndUpdate();
  }

  getDataAndUpdate() {
    this.logger.info('get data and update');

    if (this.isOverlaidEnabled) {
      this.dataService.getDataSample(this.dataSet, [this.selectedColumn.name, this.selectedGroupByColumn.name]).subscribe(rows => {
        rows = rows.filter(row => row.values[0] && row.values[1]);
        this.update(rows.map(row => row.values[0]), rows.map(row => row.values[1]));
      });
    } else {
      this.dataService.getDataSample(this.dataSet, [this.selectedColumn.name]).subscribe(rows => {
        this.update(rows.map(row => row.values[0]), null);
      });
    }
  }

  private update(values: any[], groupByValues: any[]) {
    let data = [];
    let layout = {};

    const colors = ["blue", "red", "green"];


    if (groupByValues) {
      const groupedBy = this.groupBy(values, groupByValues);
      let i = 0;
      groupedBy.forEach((value: any[], key: any) => {
        data.push({
          x: value,
          name: this.selectedGroupByColumn.name + ": " + key,
          type: 'histogram',
          opacity: 0.5,
          marker: {
            color: colors[i++],
          },
          histnorm: this.selectedHistNorm.value
        })
      });

      layout = {
        title: `Histogram "${this.selectedColumn.name}" feature`,
        barmode: "overlay",
        xaxis: {
          title: this.selectedColumn.name,
        },
        yaxis: {
          title: this.selectedHistNorm.labelText
        }
      };

    } else {
      data = [{
        x: values,
        type: 'histogram',
        histnorm: this.selectedHistNorm.value,
        cumulative: {
          enabled: this.isCumulativeEnabled
        }
      }];

      layout = {
        title: `Histogram "${this.selectedColumn.name}" feature`,
        xaxis: {
          title: this.selectedColumn.name,
        },
        yaxis: {
          title: this.selectedHistNorm.labelText
        }
      };
    }


    const chartData: ChartData = {
      data: data,
      layout: layout
    };

    this.settingsUpdated.emit(chartData);
  }

  private groupBy(values: any[], groupByValues: any[]): Map<any, any[]> {
    const map = new Map<any, any[]>();
    values.forEach((value, index) => {
      const key = groupByValues[index];
      if (map.has(key)) {
        map.get(key).push(value);
      } else {
        let array = [];
        array.push(value);
        map.set(key, array);
      }
    });

    return map;
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
