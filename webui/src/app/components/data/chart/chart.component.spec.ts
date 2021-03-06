import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ChartComponent} from './chart.component';
import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DataSet} from '../../../model/data-set.model';
import {logging} from '../../../app.logging';
import {translate} from '../../../app.translate';

describe('ChartComponent', () => {
  let component: ChartComponent;
  let fixture: ComponentFixture<ChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ChartComponent,
        StubPieSettingsComponent,
        StubHistogramSettingsComponent,
        StubScatterSettingsComponent
      ],
      imports: [
        logging,
        translate
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  xit('should be created', () => {
    expect(component).toBeTruthy();
  });
});

@Component({
  selector: 'chart-histogram-settings',
  template: ''
})
class StubHistogramSettingsComponent {
  @Input() dataSet: DataSet;
  @Output() settingsUpdated = new EventEmitter();
}

@Component({
  selector: 'chart-scatter-settings',
  template: ''
})
class StubScatterSettingsComponent {
  @Input() dataSet: DataSet;
  @Output() settingsUpdated = new EventEmitter();
}

@Component({
  selector: 'chart-pie-settings',
  template: ''
})
class StubPieSettingsComponent {
  @Input() dataSet: DataSet;
  @Output() settingsUpdated = new EventEmitter();
}
