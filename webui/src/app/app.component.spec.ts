import {async, TestBed} from '@angular/core/testing';

import {AppComponent} from './app.component';
import {NavbarComponent} from './components/shared/navbar/navbar.component';
import {RouterTestingModule} from '@angular/router/testing';
import {FooterComponent} from './components/shared/footer/footer.component';
import {TranslateModule} from '@ngx-translate/core';
import {HttpLoaderComponent} from "./components/shared/http-loader/http-loader.component";
import {LoadingIndicatorService} from "./services/loading.indicator.service";
import {LoadingIndicatorInterceptor} from "./interceptor/loading.indicator.interceptor";
import {NotifierService} from "angular-notifier";
import {CUSTOM_ELEMENTS_SCHEMA} from "@angular/core";

describe('AppComponent', () => {
  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        AppComponent,
        NavbarComponent,
        FooterComponent,
        HttpLoaderComponent
      ],
      imports: [
        RouterTestingModule,
        TranslateModule.forRoot()
      ],
      providers: [
        LoadingIndicatorService,
        LoadingIndicatorInterceptor,
        NotifierService
      ],
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ]
    }).compileComponents();
  }));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));


});
