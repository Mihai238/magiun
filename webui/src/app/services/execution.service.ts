import {Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";
import {NGXLogger} from "ngx-logger";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";

@Injectable()
export class ExecutionService {

  private readonly path = '/executions/';

  constructor(private http: HttpClient, private logger: NGXLogger) {
  }

  create(blockId: string): Observable<string> {
    this.logger.info("Creating execution for blockId " + blockId);

    const body = {
      blockId: blockId
    };

    return this.http.post(environment.baseUrl + this.path, body)
      .map((res: any) => res.id)
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }
}
