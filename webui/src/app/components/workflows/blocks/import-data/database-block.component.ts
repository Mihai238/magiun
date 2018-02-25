import {Component} from '@angular/core';
import {BlockType} from '../block-type';
import {ImportDataComponent} from './import-data.component';

@Component({
  selector: 'app-database-block',
  templateUrl: '../block.component.html',
  styleUrls: ['./import-data-block.component.scss']
})
export class DatabaseBlockComponent extends ImportDataComponent {

  constructor() {
    super();
    this.name = BlockType.DATABASE.name;
    this.type = BlockType.DATABASE.type;
    this.id = BlockType.DATABASE.type + '-' + new Date().getMilliseconds();
  }
}
