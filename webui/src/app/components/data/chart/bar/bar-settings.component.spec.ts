import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BarSettingsComponent } from './bar-settings.component';

describe('BarSettingsComponent', () => {
  let component: BarSettingsComponent;
  let fixture: ComponentFixture<BarSettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BarSettingsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BarSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
