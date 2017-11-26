import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';

@Component({
  selector: 'app-histogramm',
  templateUrl: './histogramm.component.html',
  styleUrls: ['./histogramm.component.css']
})
export class HistogrammComponent implements OnInit {

  @ViewChild('chart') chartEl: ElementRef;

  constructor() { }

  ngOnInit() {
    const element = this.chartEl.nativeElement;

    const data = [{
      x: [1, 2, 3, 4, 5, 1, 2, 4, 8, 16],
      type: 'histogram',
    }];

    Plotly.plot(element, data);
  }

}
