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
import {TrainAlgorithmResponse} from "../model/response/train.algorithm.response.model";
import {AlgorithmImplementation} from "../model/algorithm/train/algorithm.implementation.model";

@Injectable()
export class AlgorithmRestService {

  private logger: MagiunLogger;
  private readonly algorithmPath = '/algorithm';
  private readonly recommendPath = '/recommend';
  private readonly trainPath = '/train';
  private readonly removePath = '/remove/';

  constructor(private http: HttpClient, ngxLogger: NGXLogger) {
    this.logger = new MagiunLogger(AlgorithmRestService.name, ngxLogger);
  }

  recommend<T extends Algorithm>(body: RecommenderRequest): Observable<T[]> {
    this.logger.info("getting algorithm recommendations for dataset " + body.datasetId);

    return this.http.post<T[]>(environment.baseUrl + this.algorithmPath + this.recommendPath, JSON.stringify(body))
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

  train(body: TrainAlgorithmRequest): Observable<TrainAlgorithmResponse> {
    this.logger.info(`sending train request for dataset ${body.datasetId} and algorithm ${body.algorithm.name}`);

    return this.http.post<TrainAlgorithmResponse>(environment.baseUrl + this.algorithmPath + this.trainPath, JSON.stringify(body))
      .map(response => {
        response.algorithmImplementation = AlgorithmImplementation[response.algorithmImplementation];
        return <TrainAlgorithmResponse> response;
      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }

  remove(id: string): Observable<any> {
    this.logger.info(`Sending remove request for model ${id}`);

    return this.http.post(environment.baseUrl + this.algorithmPath + this.removePath + id, null)
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'))
  }
}
