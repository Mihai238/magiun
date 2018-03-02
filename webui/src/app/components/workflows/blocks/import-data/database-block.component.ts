import {Component} from '@angular/core';
import {BlockType} from '../block-type';
import {ImportDataComponent} from './import-data.component';
import {BlockService} from '../../../../services/block.service';
import {DialogService} from 'ng2-bootstrap-modal';

@Component({
  selector: 'app-block',
  templateUrl: '../block.component.html',
  styleUrls: ['./import-data-block.component.scss']
})
export class DatabaseBlockComponent extends ImportDataComponent {

  constructor(blockService: BlockService, dialogService: DialogService) {
    super(blockService, dialogService);
    this.name = BlockType.DATABASE.name;
    this.type = BlockType.DATABASE.type;
    this.id = BlockType.DATABASE.type + '-' + new Date().getMilliseconds();
  }
}
