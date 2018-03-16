import {Component} from '@angular/core';
import {BlockType} from '../../block-type';
import {RegressionBlockComponent} from './regression-block.component';
import {BlockService} from '../../../../../services/block.service';
import {DialogService} from 'ng2-bootstrap-modal';

@Component({
  selector: 'app-block',
  templateUrl: '../../block.component.html',
  styleUrls: ['./regression-block.component.scss']
})
export class PoissonRegressionBlockComponent extends RegressionBlockComponent {

  name = BlockType.POISSON_REGRESSION.name;
  id = BlockType.POISSON_REGRESSION.name + '-' + new Date().getMilliseconds();

  constructor(blockService: BlockService, dialogService: DialogService) {
    super(blockService, dialogService);
  }
}
