import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddColumnSettingsComponent } from './add-column-settings.component';

describe('AddColumnSettingsComponent', () => {
  let component: AddColumnSettingsComponent;
  let fixture: ComponentFixture<AddColumnSettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddColumnSettingsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddColumnSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
