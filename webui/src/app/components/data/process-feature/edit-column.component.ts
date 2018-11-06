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
import {Column, DataSet} from '../../../model/data-set.model';
import {NGXLogger} from "ngx-logger";
import {ExecutionService} from "../../../services/execution.service";
import {BlockRestService} from "../../../rest/block.rest.service";
import {BlockType} from "../../workflows/blocks/block-type";
import {Block} from "../../../model/block.model";

@Component({
  selector: 'data-edit-column',
  templateUrl: './edit-column.component.html',
  styleUrls: ['./edit-column.component.scss']
})
export class EditColumnComponent implements OnInit, OnChanges {

  @Input() visible: boolean;
  @Input() column: Column;
  @Input() dataSet: DataSet;

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
    const loadDataSetBlock = {
      id: "",
      type: BlockType.DATA_SET_READER.name,
      inputs: [],
      params: {"dataSetId": this.dataSet.id}
    };

    const processingBlock = this.getProcessingBlock();
    if (processingBlock) {
      this.blockRestService.createBlock(loadDataSetBlock).subscribe(loadDataBlockId => {
        processingBlock.inputs.push({blockId: loadDataBlockId, "index": 0});
        this.blockRestService.createBlock(processingBlock).subscribe(processColumnBlockId => {
          this.executionService.create(processColumnBlockId).subscribe((memDataSetId) => {
            this.resultEmitter.emit({memDataSetId: memDataSetId});
          });
        });
      });
    }
  }

  private getProcessingBlock(): Block {
    if (this.selectedActionType == ActionType.drop) {
      return {
        id: "",
        type: BlockType.DROP_COLUMNS.name,
        inputs: [],
        params: {columnName: this.column.name}
      };
    } else {
      this.logger.error('Action type "' + this.selectedActionType + '" not supported');
      return null;
    }
  }

}

export interface EditColumnResult {
  memDataSetId: string;
}

export enum ActionType {
  drop = 'Drop',
  script = 'Script'
}
