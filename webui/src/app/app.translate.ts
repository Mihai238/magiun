import {TranslateLoader, TranslateModule, TranslateStaticLoader} from 'ng2-translate';
import {Http} from '@angular/http';

export const translate = TranslateModule.forRoot({
  provide: TranslateLoader,
  useFactory: (createTranslateLoader),
  deps: [Http]
});

export function createTranslateLoader(http: Http) {
  return new TranslateStaticLoader(http, '../assets/locale', '.json');
}
