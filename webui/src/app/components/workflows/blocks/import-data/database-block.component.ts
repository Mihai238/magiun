import {Component} from '@angular/core';
import {BlockType} from '../block-type';
import {ImportDataComponent} from './import-data.component';
import {BlockService} from '../../../../services/block.service';
import {DialogService} from 'ng2-bootstrap-modal';
import {BlockComponent} from '../block.component';

@Component({
  selector: 'app-block',
  templateUrl: '../block.component.html',
  styleUrls: ['./import-data-block.component.scss']
})
export class DatabaseBlockComponent extends ImportDataComponent {

  name = BlockType.DATABASE.name;
  id = BlockType.DATABASE.name + '-' + new Date().getMilliseconds();
  i18nValue = BlockComponent.base_path + BlockType.DATABASE.i18nValue;

  constructor(blockService: BlockService, dialogService: DialogService) {
    super(blockService, dialogService);
  }
}
