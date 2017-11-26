import {Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {ChartData} from '../chart.component';

@Component({
  selector: 'app-bar',
  templateUrl: './bar.component.html',
  styleUrls: ['./bar.component.css']
})
export class BarComponent implements OnInit {

  @ViewChild('chart') chartEl: ElementRef;

  @Input() chartData: ChartData;

  constructor() { }

  ngOnInit() {
    const data = [{
      x: this.chartData.xValues,
      y: this.chartData.yValues,
      type: 'bar'
    }];

    const layout = {

    };

    Plotly.plot(this.chartEl.nativeElement, data, layout, {displayModeBar: false})

  }


}
