import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {DataService} from "../../../../services/data.service";
import {Column, DataSet} from "../../../../model/data-set.model";
import {ChartData} from "../../../../model/chart-data.model";
import {CollectionsUtils} from "../../../../util/collections.utils";

@Component({
  selector: 'chart-bar-settings',
  templateUrl: './bar-settings.component.html',
  styleUrls: ['./bar-settings.component.scss']
})
export class BarSettingsComponent implements OnInit {

  @Input() dataSet: DataSet;
  @Output() settingsUpdated = new EventEmitter<ChartData>();

  tooManyLabels: boolean;
  selectedColumn: Column;

  constructor(private dataService: DataService) {
  }

  ngOnInit() {
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
    return keyWithOccurrences.size <= 10;
  }

  private update(keyWithOccurrences: Map<any, number>): void {
    const {values, labels} = this.computeValuesAndLabels(keyWithOccurrences);

    const data = [{
      x: labels,
      y: values,
      type: 'bar'
    }];

    const layout = {
      title: 'Bar chart title'
    };

    const chartData = {
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
