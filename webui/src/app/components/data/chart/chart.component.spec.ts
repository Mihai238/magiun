import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ChartComponent, ChartData} from './chart.component';
import {Component, Input} from '@angular/core';
import {PieComponent} from './pie/pie.component';
import {PieSettingsComponent} from './pie/pie-settings.component';
import {BarSettingsComponent} from './bar/bar-settings.component';
import {HistogramSettingsComponent} from './histogram/histogram-settings.component';

describe('ChartComponent', () => {
  let component: ChartComponent;
  let fixture: ComponentFixture<ChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ChartComponent,
        HistogramStubComponent,
        BarStubComponent,
        PieComponent,
        PieSettingsComponent,
        BarSettingsComponent,
        HistogramSettingsComponent
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
