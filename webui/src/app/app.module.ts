import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {RouterModule, Routes} from '@angular/router';

import {AppComponent} from './app.component';
import {DataService} from './services/data.service';
import {DataComponent} from './components/data/data.component';
import {LoggerModule, NgxLoggerLevel} from 'ngx-logger';
import { AboutComponent } from './components/about/about.component';

const appRoutes: Routes = [
  {path: '', component: DataComponent},
  {path: 'about', component: AboutComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    DataComponent,
    AboutComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    LoggerModule.forRoot({level: NgxLoggerLevel.INFO, serverLogLevel: NgxLoggerLevel.OFF}),
    RouterModule.forRoot(appRoutes)
  ],
  providers: [
    DataService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
