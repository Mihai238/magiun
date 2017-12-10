import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewColumnSettingsComponent } from './new-column-settings.component';

describe('NewColumnSettingsComponent', () => {
  let component: NewColumnSettingsComponent;
  let fixture: ComponentFixture<NewColumnSettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewColumnSettingsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewColumnSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
