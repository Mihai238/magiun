import {Injectable} from "@angular/core";
import {MagiunLogger} from "../util/magiun.logger";
import {HttpClient} from "@angular/common/http";
import {NGXLogger} from "ngx-logger";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {Algorithm} from "../model/algorithm/algorithm.model";
import {RecommenderRequest} from "../model/recommender-request.model";
import {AlgorithmParameter} from "../model/algorithm/algorithm.parameter.model";
import {TrainAlgorithmRequest} from "../model/algorithm/train/train.algorithm.request.model";

@Injectable()
export class RecommenderRestService {

  private logger: MagiunLogger;
  private readonly recommenderPath = '/recommender';
  private readonly algoRecommendationsPath = '/algo-recommendations';
  private readonly trainAlgorithmPath = '/train';

  constructor(private http: HttpClient, ngxLogger: NGXLogger) {
    this.logger = new MagiunLogger(RecommenderRestService.name, ngxLogger);
  }

  recommend<T extends Algorithm>(body: RecommenderRequest): Observable<T[]> {
    this.logger.info("getting algorithm recommendations for dataset " + body.datasetId);

    return this.http.post<T[]>(environment.baseUrl + this.recommenderPath + this.algoRecommendationsPath, JSON.stringify(body))
      .map(algorithms => algorithms.map(algorithm => {
        let a = <Algorithm>Object.values(algorithm)[0];
        a.implementation = Object.keys(algorithm)[0];
        if (a.parameters != null) {
          a.parameters = a.parameters.map(p => <AlgorithmParameter<any>>Object.values(p)[0]);
        } else {
          a.parameters = [];
        }
        return a
      }))
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'))
  }

  train(body: TrainAlgorithmRequest): Observable<any> {
    this.logger.info(`sending train request for dataset ${body.datasetId} and algorithm ${body.algorithm.name}`);

    return this.http.post(environment.baseUrl + this.recommenderPath + this.trainAlgorithmPath, JSON.stringify(body))
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }
}
