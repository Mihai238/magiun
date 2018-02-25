import {Component} from '@angular/core';
import {BlockType} from '../block-type';
import {ImportDataComponent} from './import-data.component';
import {BlockService} from '../../../../services/block.service';

@Component({
  selector: 'app-database-block',
  templateUrl: '../block.component.html',
  styleUrls: ['./import-data-block.component.scss']
})
export class FileBlockComponent extends ImportDataComponent {

  constructor(blockService: BlockService) {
    super(blockService);
    this.name = BlockType.FILE.name;
    this.type = BlockType.FILE.type;
    this.id = BlockType.FILE.type + '-' + new Date().getMilliseconds();
  }
}
