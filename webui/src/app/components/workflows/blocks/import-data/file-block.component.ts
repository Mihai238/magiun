import {Component} from '@angular/core';
import {BlockType} from '../block-type';
import {ImportDataComponent} from './import-data.component';

@Component({
  selector: 'app-database-block',
  templateUrl: '../block.component.html',
  styleUrls: ['./import-data-block.component.scss']
})
export class FileBlockComponent extends ImportDataComponent {

  constructor() {
    super();
    this.name = BlockType.FILE.name;
    this.code = BlockType.FILE.code;
    this.id = BlockType.FILE.code + '-' + new Date().getMilliseconds();
  }
}
