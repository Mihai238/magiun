import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-data',
  templateUrl: './data.component.html',
  styleUrls: ['./data.component.css']
})
export class DataComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  loadDataSet(dataSetName: String) {
    console.log('Loading data set ' + dataSetName);
  }
}
