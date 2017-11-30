import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {ChartData} from '../chart.component';

@Component({
  selector: 'chart-pie',
  templateUrl: './pie.component.html',
  styleUrls: ['./pie.component.css']
})
export class PieComponent implements OnInit {

  @ViewChild('chart') chartEl: ElementRef;

  @Input() chartData: ChartData;

  constructor() { }

  ngOnInit() {
    const data = [{
      values: this.chartData.xValues,
      type: 'pie'
    }];

    const layout = {

    };

    Plotly.plot(this.chartEl.nativeElement, data, layout, {displayModeBar: false});
  }

}
