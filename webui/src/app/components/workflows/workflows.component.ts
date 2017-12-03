import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-workflows',
  templateUrl: './workflows.component.html',
  styleUrls: ['./workflows.component.css']
})
export class WorkflowsComponent implements OnInit {

  private showPlaceholder = true;

  constructor() { }

  ngOnInit() {
  }

  private addDropItem(event) {
    if (this.showPlaceholder) {
      this.showPlaceholder = false;
    }
    console.log(event)
  }

}
