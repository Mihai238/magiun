import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {BlockComponent} from '../components/workflows/blocks/block.component';
import {HttpUtils} from '../util/http.utils';
import {HttpClient} from '@angular/common/http';

@Injectable()
export class BlockController {

  private blocksUrl = environment.baseUrl + '/blocks/';

  constructor(private http: HttpClient) {}

  upsertBlock(block: BlockComponent): void {
    this.http.post(
      this.blocksUrl,
      HttpUtils.createBlockComponentBodyJson(block),
      HttpUtils.optionsOnlyWithHeaders()
    ).subscribe(() => {});
  }

  deleteBlock(blockId: string): void {
    this.http.delete(
      this.blocksUrl.concat(blockId),
      HttpUtils.optionsOnlyWithHeaders()
    ).subscribe(() => {});
  }

}
