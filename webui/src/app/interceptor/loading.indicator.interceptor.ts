import {HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {LoadingIndicatorService} from "../services/loading.indicator.service";

@Injectable()
export class LoadingIndicatorInterceptor implements HttpInterceptor {

  constructor(private loadingIndicatorService: LoadingIndicatorService) {}

  intercept (request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.loadingIndicatorService.onStarted(request);

    return next.handle(request)
      .finally(() => this.loadingIndicatorService.onFinished(request));
  }

}
