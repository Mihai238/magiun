import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  currTabIndex = 0;

  tabs = [
    {text: 'Data visualization', link: '/'},
    {text: 'Workflows', link: '/workflows'},
    {text: 'About', link: '/about'}
  ];
}
