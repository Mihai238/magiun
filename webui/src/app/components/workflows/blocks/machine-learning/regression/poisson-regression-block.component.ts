import {Component} from '@angular/core';
import {BlockType} from '../../block-type';
import {RegressionBlockComponent} from './regression-block.component';
import {BlockService} from '../../../../../services/block.service';

@Component({
  selector: 'app-poisson-regression-block',
  templateUrl: '../../block.component.html',
  styleUrls: ['./regression-block.component.scss']
})
export class PoissonRegressionBlockComponent extends RegressionBlockComponent {

  constructor(blockService: BlockService) {
    super(blockService);
    this.name = BlockType.POISSON_REGRESSION.name;
    this.type = BlockType.POISSON_REGRESSION.type;
    this.id = BlockType.POISSON_REGRESSION.type + '-' + new Date().getMilliseconds();
  }
}
