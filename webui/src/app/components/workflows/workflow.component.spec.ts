import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkflowComponent } from './workflow.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import {DragDropDirectiveModule} from 'angular4-drag-drop';
import {LinearRegressionBlockComponent} from './blocks/machine-learning/regression/linear-regression-block.component';
import {PoissonRegressionBlockComponent} from './blocks/machine-learning/regression/poisson-regression-block.component';
import {translate} from '../../app.translate';
import {logging} from '../../app.logging';

describe('WorkflowComponent', () => {
  let component: WorkflowComponent;
  let fixture: ComponentFixture<WorkflowComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        WorkflowComponent ,
        SidebarComponent,
        LinearRegressionBlockComponent,
        PoissonRegressionBlockComponent
      ],
      imports: [
        DragDropDirectiveModule,
        logging,
        translate
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WorkflowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
