import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {BlockComponent} from '../components/workflows/blocks/block.component';
import {HttpUtils} from '../util/http.utils';
import {HttpClient} from '@angular/common/http';
import {NGXLogger} from 'ngx-logger';
import {Block} from "../model/block.model";
import {Observable} from "rxjs";

@Injectable()
export class BlockRestService {

  private blocksUrl = environment.baseUrl + '/blocks/';

  constructor(private http: HttpClient, private logger: NGXLogger) {}

  createBlock(block: Block): Observable<string> {
    this.logger.info('Creating block: ' + JSON.stringify(block));

    return this.http.post(this.blocksUrl, block)
      .map((res: any) => res.id)
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

  upsertBlock(block: BlockComponent): void {
    const blockJson = HttpUtils.createBlockComponentBodyJson(block);
    this.logger.info('Upserting block: ' + blockJson);
    this.http.post(
      this.blocksUrl,
      blockJson,
      HttpUtils.optionsOnlyWithHeaders()
    ).subscribe(() => {});
  }

  deleteBlock(blockId: string): void {
    this.logger.info('Deleting block: ' + blockId);
    this.http.delete(
      this.blocksUrl.concat(blockId),
      HttpUtils.optionsOnlyWithHeaders()
    ).subscribe(() => {});
  }

}
