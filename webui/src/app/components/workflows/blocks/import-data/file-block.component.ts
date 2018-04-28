import {Component} from '@angular/core';
import {BlockType} from '../block-type';
import {ImportDataComponent} from './import-data.component';
import {BlockService} from '../../../../services/block.service';
import {BlockParameter} from '../block-parameter';
import {DialogService} from 'ng2-bootstrap-modal';
import {BlockComponent} from '../block.component';

@Component({
  selector: 'app-block',
  templateUrl: '../block.component.html',
  styleUrls: ['./import-data-block.component.scss']
})
export class FileBlockComponent extends ImportDataComponent {

  name = BlockType.FILE.name;
  id = BlockType.FILE.name + '-' + new Date().getMilliseconds();
  i18nValue = BlockComponent.base_path + BlockType.FILE.i18nValue;

  constructor(blockService: BlockService, dialogService: DialogService) {
    super(blockService, dialogService);
    this.configurationParameters = [BlockParameter.FILE_URL];
  }
}
