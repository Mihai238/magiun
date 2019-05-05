import {HttpRequest} from "@angular/common/http";
import {EventEmitter, Injectable} from "@angular/core";

@Injectable()
export class LoadingIndicatorService {

  onLoadingChanged: EventEmitter<boolean> = new EventEmitter<boolean>();

  /**
   * Stores all currently active requests
   */
  private requests: HttpRequest<any>[] = [];

  /**
   * Adds request to the storage and notifies observers
   */
  onStarted(request: HttpRequest<any>): void {
    this.requests.push(request);
    if (request.url.indexOf("/remove/") < 0) {
      this.notify();
    }
  }

  /**
   * Removes request from the storage and notifies observers
   */
  onFinished(request: HttpRequest<any>): void {
    const index = this.requests.indexOf(request);
    if (index !== -1) {
      this.requests.splice(index, 1);
    }
    this.notify();
  }

  /**
   * Notifies observers about whether there are any requests on fly
   */
  private notify(): void {
    this.onLoadingChanged.emit(this.requests.length !== 0);
  }

}
