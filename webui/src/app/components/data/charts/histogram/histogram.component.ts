import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';

@Component({
  selector: 'app-histogram',
  templateUrl: './histogram.component.html',
  styleUrls: ['./histogram.component.css']
})
export class HistogramComponent implements OnInit {

  @ViewChild('chart') chartEl: ElementRef;
  @Input() values: any;

  constructor() { }

  ngOnInit() {
    const element = this.chartEl.nativeElement;

    const data = [{
      x: this.values,
      type: 'histogram'
    }];

    const layout = {
      title: 'some title',
      xaxis: {
        title: 'x axis title',
      },
      yaxis: {
        title: 'y axis title'
      }
    };

    Plotly.plot(element, data, layout, {displayModeBar: false});
  }

}
