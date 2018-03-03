import {Component} from '@angular/core';
import {BlockType} from '../block-type';
import {ImportDataComponent} from './import-data.component';
import {BlockService} from '../../../../services/block.service';
import {BlockParameter} from '../block-parameter';
import {DialogService} from 'ng2-bootstrap-modal';

@Component({
  selector: 'app-block',
  templateUrl: '../block.component.html',
  styleUrls: ['./import-data-block.component.scss']
})
export class FileBlockComponent extends ImportDataComponent {

  constructor(blockService: BlockService, dialogService: DialogService) {
    super(blockService, dialogService);
    this.name = BlockType.FILE.name;
    this.type = BlockType.FILE.type;
    this.id = BlockType.FILE.type + '-' + new Date().getMilliseconds();
    this.configurationParameters = [BlockParameter.FILE_URL];
  }
}