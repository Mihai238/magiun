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
import {ExecutionService} from "../../../services/execution.service";
import {BlockRestService} from "../../../rest/block.rest.service";

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
  selectedActionType: ActionType;

  constructor(private logger: NGXLogger,
              private executionService: ExecutionService,
              private blockRestService: BlockRestService) {
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
    this.selectedActionType = ActionType[actionTypeString];
  }

  actionTypes(): Array<string> {
    return Object.keys(this.ActionType);
  }

  onClickCancel(): void {
    this.resultEmitter.emit({memDataSetId: null});
  }

  onClickExecute(): void {
    const type = "DropColumn";
    const block = {
      id: "",
      type: type,
      inputs: [{blockId: "id-2", "index": 0}],
      params: {columnName: this.column.name}
    };

    this.blockRestService.createBlock(block).subscribe(blockId => {
      this.executionService.create(blockId).subscribe((memDataSetId) => {
        this.resultEmitter.emit({memDataSetId: memDataSetId});
      });
    });
  }

}

export interface EditColumnResult {
  memDataSetId: string;
}

export enum ActionType {
  drop = 'Drop',
  script = 'Script'
}
