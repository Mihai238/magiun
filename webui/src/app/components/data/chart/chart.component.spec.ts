import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ChartComponent} from './chart.component';
import {PieSettingsComponent} from './pie/pie-settings.component';
import {Component, EventEmitter, Input, Output} from '@angular/core';
import {DataSet} from '../../../model/data-set';
import {logging} from '../../../app.logging';

describe('ChartComponent', () => {
  let component: ChartComponent;
  let fixture: ComponentFixture<ChartComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ChartComponent,
        PieSettingsComponent,
        StubHistogramSettingsComponent,
        StubScatterSettingsComponent
      ],
      imports: [
        logging
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
