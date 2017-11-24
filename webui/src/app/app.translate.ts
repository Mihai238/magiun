import {HttpClient} from '@angular/common/http';
import {TranslateLoader, TranslateModule} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';

export const translate = TranslateModule.forRoot({
  loader: {
    provide: TranslateLoader,
    useFactory: (createTranslateLoader),
    deps: [HttpClient]
  }
});

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, '../assets/locale/', '.json');
}
