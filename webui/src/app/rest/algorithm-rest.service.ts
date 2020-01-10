import {Injectable} from "@angular/core";
import {MagiunLogger} from "../util/magiun.logger";
import {HttpClient} from "@angular/common/http";
import {NGXLogger} from "ngx-logger";
import {environment} from "../../environments/environment";
import {Observable} from "rxjs";
import {Algorithm} from "../model/algorithm/algorithm.model";
import {RecommenderRequest} from "../model/request/recommender.request.model";
import {AlgorithmParameter} from "../model/algorithm/algorithm.parameter.model";
import {TrainAlgorithmRequest} from "../model/request/train.algorithm.request.model";
import {TrainAlgorithmResponse} from "../model/response/train.algorithm.response.model";
import {AlgorithmImplementation} from "../model/algorithm/algorithm.implementation.model";
import {RecommenderResponse} from "../model/response/recommender.response.model";

@Injectable()
export class AlgorithmRestService {

  private logger: MagiunLogger;
  private readonly algorithmPath = '/algorithm';
  private readonly recommendPath = '/recommend';
  private readonly trainPath = '/train';
  private readonly savePath = '/save/';
  private readonly removePath = '/remove/';
  private readonly likePath = '/like/';
  private readonly dislikePath = '/dislike/';

  constructor(private http: HttpClient, ngxLogger: NGXLogger) {
    this.logger = new MagiunLogger(AlgorithmRestService.name, ngxLogger);
  }

  recommend(body: RecommenderRequest): Observable<RecommenderResponse> {
    this.logger.info("getting algorithm recommendations for dataset " + body.datasetId);

    return this.http.post<RecommenderResponse>(environment.baseUrl + this.algorithmPath + this.recommendPath, JSON.stringify(body))
      .map(response => {

        let rec = this.convertAlgorithmsParameters(response.recommendations);
        let nonRec = this.convertAlgorithmsParameters(response.nonRecommendations);

        return <RecommenderResponse> {
          requestId: response.requestId,
          recommendations: rec,
          nonRecommendations: nonRec,
          message: response.message
        };
      })
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

  save(id: string): Observable<any> {
    this.logger.info(`Sending save request for model ${id}`);

    return this.http.post(environment.baseUrl + this.algorithmPath + this.savePath + id, null)
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'))
  }

  remove(id: string): Observable<any> {
    this.logger.info(`Sending remove request for model ${id}`);

    return this.http.post(environment.baseUrl + this.algorithmPath + this.removePath + id, null)
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'))
  }

  like(requestId: string, recommendationId): Observable<any> {
    return this.http.post(environment.baseUrl + this.algorithmPath + this.likePath + requestId + "/" + recommendationId, null)
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'))
  }

  dislike(requestId: string, recommendationId): Observable<any> {
    return this.http.post(environment.baseUrl + this.algorithmPath + this.dislikePath + requestId + "/" + recommendationId, null)
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'))
  }

  convertAlgorithmsParameters(algorithms:  Algorithm[]): Algorithm[] {
    return algorithms.map(algorithm => {
      let a = <Algorithm>Object.values(algorithm)[0];
      a.implementation = Object.keys(algorithm)[0];
      if (a.parameters != null) {
        a.parameters = a.parameters.map(p => <AlgorithmParameter<any>>Object.values(p)[0]);
      } else {
        a.parameters = [];
      }
      return a
    });
  }
}
