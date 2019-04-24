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
import {eraseStyles} from "@angular/animations/browser/src/util";
import {error} from "util";

@Injectable()
export class AlgorithmRestService {

  private logger: MagiunLogger;
  private readonly recommenderPath = '/algorithm';
  private readonly algoRecommendationsPath = '/recommend';
  private readonly trainAlgorithmPath = '/train';

  constructor(private http: HttpClient, ngxLogger: NGXLogger) {
    this.logger = new MagiunLogger(AlgorithmRestService.name, ngxLogger);
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

  train(body: TrainAlgorithmRequest): Observable<TrainAlgorithmResponse> {
    this.logger.info(`sending train request for dataset ${body.datasetId} and algorithm ${body.algorithm.name}`);

    return this.http.post<TrainAlgorithmResponse>(environment.baseUrl + this.recommenderPath + this.trainAlgorithmPath, JSON.stringify(body))
      .map(response => {
        return <TrainAlgorithmResponse> {
          id: response.id,
          algorithmImplementation: AlgorithmImplementation[response.algorithmImplementation],
          intercept: response.intercept,
          coefficients: response.coefficients,
          degreesOfFreedom: response.degreesOfFreedom,
          explainedVariance: response.explainedVariance,
          meanAbsoluteError: response.meanAbsoluteError,
          meanSquaredError: response.meanSquaredError,
          rSquared: response.rSquared,
          rSquaredAdjusted: response.rSquaredAdjusted,
          rootMeanSquaredError: response.rootMeanSquaredError,
          fittedValues: response.fittedValues,
          residuals: response.residuals,
          dataSample: response.dataSample,
          errorMessage: response.errorMessage
        };

      })
      .catch((error: any) => Observable.throw(error.json().error || 'Server error'));
  }
}
