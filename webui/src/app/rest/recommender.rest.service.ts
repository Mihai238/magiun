import {Injectable} from "@angular/core";
import {MagiunLogger} from "../util/magiun.logger";
import {HttpClient} from "@angular/common/http";
import {NGXLogger} from "ngx-logger";

@Injectable()
export class RecommenderRestService {

  private logger: MagiunLogger;

  constructor(private http: HttpClient, ngxLogger: NGXLogger) {
    this.logger = new MagiunLogger(RecommenderRestService.name, ngxLogger);
  }
}
