import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HistogrammComponent } from './histogramm.component';

describe('HistogrammComponent', () => {
  let component: HistogrammComponent;
  let fixture: ComponentFixture<HistogrammComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HistogrammComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HistogrammComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
