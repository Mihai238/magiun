import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ChartComponent, ChartData} from './chart.component';
import {Component, Input} from '@angular/core';

describe('ChartComponent', () => {
  let component: ChartComponent;
  let fixture: ComponentFixture<ChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ChartComponent,
        HistogramStubComponent,
        BarStubComponent
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});

@Component({
  selector: 'app-histogram',
  template: ''
})
class HistogramStubComponent {
  @Input() chartData: ChartData;
}

@Component({
  selector: 'app-bar',
  template: ''
})
class BarStubComponent {
  @Input() chartData: ChartData;
}
