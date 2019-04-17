import {Component} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {LoadingIndicatorService} from "./services/loading.indicator.service";
import 'rxjs/add/operator/finally';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  loading: boolean = false;

  constructor(private translate: TranslateService, private loadingIndicatorService: LoadingIndicatorService) {
    this.configureTranslate();
    this.configureLoadingIndicator();
  }

  private configureTranslate(): void {
    this.translate.setDefaultLang('en');
    this.translate.use(this.translate.getBrowserLang());
  }

  private configureLoadingIndicator(): void {
    this.loadingIndicatorService
      .onLoadingChanged
      .subscribe(isLoading => {
        this.loading = isLoading;
      });
  }
}
