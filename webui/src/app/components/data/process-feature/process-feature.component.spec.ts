import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProcessFeatureComponent } from './process-feature.component';

describe('ProcessFeatureComponent', () => {
  let component: ProcessFeatureComponent;
  let fixture: ComponentFixture<ProcessFeatureComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProcessFeatureComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProcessFeatureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
