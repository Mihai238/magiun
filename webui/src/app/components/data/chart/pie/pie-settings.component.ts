import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ChartData} from '../../../../model/chart-data.model';
import {DataService} from '../../../../services/data.service';
import {Column, DataSet} from '../../../../model/data-set.model';
import {CollectionsUtils} from '../../../../util/collections.utils';

@Component({
  selector: 'chart-pie-settings',
  templateUrl: './pie-settings.component.html'
})
export class PieSettingsComponent implements OnInit {

  @Input() dataSet: DataSet;
  @Output() settingsUpdated = new EventEmitter();

  readonly MAX_LABELS = 30;

  tooManyLabels: boolean;
  selectedColumn: Column;

  constructor(private dataService: DataService) {
  }

  ngOnInit(): void {
    this.tooManyLabels = false;
  }

  onUpdateColumn(column: Column) {
    this.selectedColumn = column;
    this.getDataAndUpdate();
  }

  private getDataAndUpdate() {
    this.dataService.getDataSample(this.dataSet, [this.selectedColumn.name])
      .subscribe(dataRows => {
        const keys: any[] = dataRows.map(row => row.values[0]);

        const keyWithOccurrences = CollectionsUtils.countOccurrences(keys);
        if (this.check(keyWithOccurrences)) {
          this.update(keyWithOccurrences);
          this.tooManyLabels = false;
        } else {
          this.tooManyLabels = true;
        }
      });
  }

  private check(keyWithOccurrences: Map<any, number>): boolean {
    return keyWithOccurrences.size <= this.MAX_LABELS;
  }

  private update(keyWithOccurrences: Map<any, number>) {
    const {values, labels} = this.computeValuesAndLabels(keyWithOccurrences);

    const data = [{
      values: values,
      labels: labels,
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

  private computeValuesAndLabels(keyWithOccurrences: Map<any, number>) {
    const values: number[] = [];
    const labels: any[] = [];

    keyWithOccurrences.forEach((occurrences, key) => {
      values.push(occurrences);
      labels.push(key);
    });
    return {values, labels};
  }
}
