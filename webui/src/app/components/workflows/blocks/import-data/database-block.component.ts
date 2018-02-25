import {Component} from '@angular/core';
import {BlockComponent} from '../block.component';
import {BlockType} from '../block-type';

@Component({
  selector: 'app-database-block',
  templateUrl: '../block.component.html',
  styleUrls: ['./import-data-block.component.scss']
})
export class DatabaseBlockComponent extends BlockComponent {
  name: string;
  id: string;
  code: string;

  constructor() {
    super();
    this.name = BlockType.DATABASE.name;
    this.code = BlockType.DATABASE.code;
    this.id = BlockType.DATABASE.code + '-' + new Date().getMilliseconds();
  }
}
