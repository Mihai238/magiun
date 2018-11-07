import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ActionType, EditColumnComponent} from './edit-column.component';
import {logging} from "../../../app.logging";
import {DataService} from "../../../services/data.service";
import {ExecutionService} from "../../../services/execution.service";
import {BlockRestService} from "../../../rest/block.rest.service";
import {FormsModule} from "@angular/forms";
import {HttpClientModule} from "@angular/common/http";

describe('EditColumnComponent', () => {

  let component: EditColumnComponent;
  let fixture: ComponentFixture<EditColumnComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        EditColumnComponent,
      ],
      imports: [
        FormsModule,
        HttpClientModule,
        logging
      ],
      providers: [
        DataService,
        ExecutionService,
        BlockRestService
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditColumnComponent);
    component = fixture.componentInstance;
    component.column = {index: 0, name: "SomeColumnName", type: undefined};
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should only show relevant info based on action type', () => {
    component.onActionTypeSelected('drop');
    expect(component.selectedActionType).toBe(ActionType.drop);

    component.onActionTypeSelected('script');
    expect(component.selectedActionType).toBe(ActionType.script);
  })
});
