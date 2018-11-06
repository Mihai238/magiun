import {
  Component, ElementRef, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges,
  ViewChild
} from '@angular/core';
import {DataSet} from "../../../model/data-set.model";
import {BlockType} from "../../workflows/blocks/block-type";
import {ExecutionService} from "../../../services/execution.service";
import {BlockRestService} from "../../../rest/block.rest.service";

@Component({
  selector: 'data-add-column-settings',
  templateUrl: './add-column-settings.component.html',
  styleUrls: ['./add-column-settings.component.scss']
})
export class AddColumnSettingsComponent implements OnInit, OnChanges {

  @Input() visible: boolean;
  @Input() dataSet: DataSet;

  @Output() resultEmitter = new EventEmitter();

  @ViewChild('modalActivator') modalActivatorEl: ElementRef;

  columnName: string;
  expression: string;

  constructor(private executionService: ExecutionService,
              private blockRestService: BlockRestService) {
  }

  ngOnInit() {
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (this.visible) {
      this.modalActivatorEl.nativeElement.click();
    }
  }

  onClickExecute(): void {
    const loadDataSetBlock = {
      id: "",
      type: BlockType.DATA_SET_READER.name,
      inputs: [],
      params: {"dataSetId": this.dataSet.id}
    };

    const addColumnBlock = {
      id: "",
      type: BlockType.ADD_COLUMN.name,
      inputs: [],
      params: {newColumnName: this.columnName, expression: this.expression}
    };

    this.blockRestService.createBlock(loadDataSetBlock).subscribe(loadDataBlockId => {
      addColumnBlock.inputs.push({blockId: loadDataBlockId, "index": 0});
      this.blockRestService.createBlock(addColumnBlock).subscribe(addColBlockId => {
        this.executionService.create(addColBlockId).subscribe(memDataSetId => {
          this.resultEmitter.emit({memDataSetId: memDataSetId});
        }, err => {
          this.resultEmitter.emit({});
        });
      })
    });
  }

  onClickCancel(): void {
    this.resultEmitter.emit({});
  }

}

export interface NewColumnResult {
  memDataSetId: string;
}
