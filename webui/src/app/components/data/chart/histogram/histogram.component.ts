import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {ChartData} from '../chart.component';

@Component({
  selector: 'app-histogram',
  templateUrl: './histogram.component.html',
  styleUrls: ['./histogram.component.css']
})
export class HistogramComponent implements OnInit {

  @ViewChild('chart') chartEl: ElementRef;

  @Input() chartData: ChartData;
  @Input() xLabel: string;
  @Input() yLabel: string;

  constructor() {
  }

  ngOnInit() {
    const data = [{
      x: this.chartData.xValues,
      type: 'histogram'
    }];

    const layout = {
      title: 'Histogram ' + (this.xLabel || '') + ' - ' + (this.yLabel || ''),
      autosize: true,
      xaxis: {
        title: this.xLabel,
      },
      yaxis: {
        title: this.yLabel
      }
    };

    Plotly.plot(this.chartEl.nativeElement, data, layout, {displayModeBar: false});
  }

}
