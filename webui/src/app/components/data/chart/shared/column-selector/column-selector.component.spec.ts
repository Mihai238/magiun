import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ColumnSelectorComponent} from './column-selector.component';
import {Column, ColumnType} from '../../../../../model/data-set';
import {By} from '@angular/platform-browser';
import {DebugElement} from '@angular/core';
import {translate} from '../../../../../app.translate';
import {HttpClientModule} from '@angular/common/http';

describe('ColumnSelectorComponent', () => {
  let component: ColumnSelectorComponent;
  let fixture: ComponentFixture<ColumnSelectorComponent>;
  let dropDownElem: DebugElement;

  const columns: Column[] = [
    {
      index: 1,
      name: 'col1',
      type: ColumnType.double
    },
    {
      index: 2,
      name: 'col2',
      type: ColumnType.int
    }
  ];


  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ColumnSelectorComponent
      ],
      imports: [
        translate,
        HttpClientModule
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ColumnSelectorComponent);
    component = fixture.componentInstance;
    dropDownElem = fixture.debugElement.query(By.css('#column-selector-dropdown'));

    component.columns = columns;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should contain col1', () => {
    const dropDownText = dropDownElem.nativeElement.textContent;
    expect(dropDownText).toContain('col1');
  });

  it('should change to col2', () => {
    component.onSelectColumn(columns[1]);
    fixture.detectChanges();

    const dropDownText = dropDownElem.nativeElement.textContent;
    expect(dropDownText).toContain('col2');
  });

  it('should emit event that column changed', () => {
    let column: Column;
    component.columnUpdated.subscribe(col => column = col);

    component.onSelectColumn(columns[1]);
    fixture.detectChanges();

    // noinspection JSUnusedAssignment
    expect(column.name).toBe('col2');
  });

});
