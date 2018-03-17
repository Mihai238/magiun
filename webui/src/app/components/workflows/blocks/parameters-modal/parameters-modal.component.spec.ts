import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {ParametersModalComponent} from './parameters-modal.component';
import {BootstrapModalModule} from 'ng2-bootstrap-modal';
import {translate} from '../../../../app.translate';
import {logging} from '../../../../app.logging';
import {Component, EventEmitter, Input, Output} from '@angular/core';
import {BlockParameter} from '../block-parameter';

describe('ParametersModal', () => {
  let component: ParametersModalComponent;
  let fixture: ComponentFixture<ParametersModalComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ParametersModalComponent,
        StubCheckboxParameterComponent,
        StubFileParameterComponent,
        StubInputParameterComponent,
        StubMultiInputParameterComponent,
        StubSelectParameterComponent
      ],
      imports: [
        logging,
        translate,
        BootstrapModalModule
      ]
    }).compileComponents()
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ParametersModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
   });

 it('should be created', () => {
   expect(component).toBeTruthy();
 });
});

@Component({
  selector: 'app-checkbox-parameter',
  template: '',
})
class StubCheckboxParameterComponent {
  @Input() parameter: BlockParameter<any>;
  @Output('onValueChanged') onValueChanged = new EventEmitter<any>();
}

@Component({
  selector: 'app-file-parameter',
  template: '',
})
class StubFileParameterComponent {
  @Input() parameter: BlockParameter<any>;
  @Output('onValueChanged') onValueChanged = new EventEmitter<any>();
}

@Component({
  selector: 'app-input-parameter',
  template: '',
})
class StubInputParameterComponent {
  @Input() parameter: BlockParameter<any>;
  @Output('onValueChanged') onValueChanged = new EventEmitter<any>();
}

@Component({
  selector: 'app-select-component',
  template: '',
})
class StubSelectParameterComponent {
  @Input() parameter: BlockParameter<any>;
  @Output('onValueChanged') onValueChanged = new EventEmitter<any>();
}

@Component({
  selector: 'app-multi-input-parameter',
  template: '',
})
class StubMultiInputParameterComponent {
  @Input() parameter: BlockParameter<any>;
  @Output('onValueChanged') onValueChanged = new EventEmitter<any>();
}
