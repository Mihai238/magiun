import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { WorkflowsComponent } from './workflows.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import {DragDropDirectiveModule} from 'angular4-drag-drop';
import {LinearRegressionBlockComponent} from './blocks/machine-learning/regression/linear-regression-block.component';
import {PoissonRegressionBlockComponent} from './blocks/machine-learning/regression/poisson-regression-block.component';
import {translate} from '../../app.translate';
import {logging} from '../../app.logging';

describe('WorkflowsComponent', () => {
  let component: WorkflowsComponent;
  let fixture: ComponentFixture<WorkflowsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        WorkflowsComponent ,
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
    fixture = TestBed.createComponent(WorkflowsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });
});
