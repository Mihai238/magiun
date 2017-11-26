import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';

@Component({
  selector: 'app-histogram',
  templateUrl: './histogram.component.html',
  styleUrls: ['./histogram.component.css']
})
export class HistogramComponent implements OnInit {

  @ViewChild('chart') chartEl: ElementRef;

  @Input() values: any;
  @Input() xLabel: string;
  @Input() yLabel: string;

  constructor() {
  }

  ngOnInit() {
    const element = this.chartEl.nativeElement;

    const data = [{
      x: this.values,
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

    Plotly.plot(element, data, layout, {displayModeBar: false});
  }

}
