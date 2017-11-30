import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HistogramSettingsComponent } from './histogram-settings.component';

describe('HistogramSettingsComponent', () => {
  let component: HistogramSettingsComponent;
  let fixture: ComponentFixture<HistogramSettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HistogramSettingsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HistogramSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
