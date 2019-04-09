import {Injectable} from "@angular/core";
import {MagiunLogger} from "../util/magiun.logger";
import {HttpClient} from "@angular/common/http";
import {NGXLogger} from "ngx-logger";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {RecommenderRequest} from "../model/recommender-request.model";

@Injectable()
export class RecommenderRestService {

  private logger: MagiunLogger;
  private readonly recommenderPath = '/recommender';
  private readonly algoRecommendationsPath = '/algo-recommendations';

  constructor(private http: HttpClient, ngxLogger: NGXLogger) {
    this.logger = new MagiunLogger(RecommenderRestService.name, ngxLogger);
  }

  recommend(body: RecommenderRequest): Observable<string[]> {
    this.logger.info("getting algorithm recommendations for dataset " + body.datasetId);

    return this.http.post(environment.baseUrl + this.recommenderPath + this.algoRecommendationsPath, JSON.stringify(body))
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'))
  }
}
