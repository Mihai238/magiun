import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges,
  ViewChild
} from '@angular/core';
import {Column} from '../../../model/data-set.model';
import {NGXLogger} from "ngx-logger";
import {DataService} from "../../../services/data.service";

@Component({
  selector: 'data-edit-column',
  templateUrl: './edit-column.component.html',
  styleUrls: ['./edit-column.component.scss']
})
export class EditColumnComponent implements OnInit, OnChanges {

  @Input() visible: boolean;
  @Input() column: Column;

  @Output() resultEmitter = new EventEmitter<EditColumnResult>();

  @ViewChild('modalActivator') modalActivatorEl: ElementRef;

  public ActionType = ActionType;
  currActionType: ActionType;

  constructor(private logger: NGXLogger,
              private dataService: DataService) {
  }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.visible) {
      this.modalActivatorEl.nativeElement.click();
    }
  }

  onActionTypeSelected(actionTypeString: string) {
    this.logger.info('EditColumnComponent: action type selected ' + actionTypeString);
    this.currActionType = ActionType[actionTypeString];
  }

  actionTypes(): Array<string> {
    return Object.keys(this.ActionType);
  }

  onClickCancel(): void {
    this.resultEmitter.emit({executed: false});
  }

  onClickExecute(): void {
    this.resultEmitter.emit({executed: true});
  }

}

export interface EditColumnResult {
  executed: boolean;
}

export enum ActionType {
  drop = 'Drop',
  script = 'Script'
}
