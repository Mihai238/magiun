import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css']
})
export class ChartComponent implements OnInit {

  histogramValues;

  constructor() { }

  ngOnInit() {
    this.histogramValues = [1, 2, 3, 0, 9 , 12];
  }

}
