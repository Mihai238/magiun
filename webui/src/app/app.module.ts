import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {RouterModule, Routes} from '@angular/router';

import {AppComponent} from './app.component';
import {DataService} from './services/data.service';
import {DataComponent} from './components/data/data.component';
import {LoggerModule, NgxLoggerLevel} from 'ngx-logger';
import {AboutComponent} from './components/about/about.component';
import {WorkflowsComponent} from './components/workflows/workflows.component';
import {PageNotFoundComponent} from './components/page-not-found/page-not-found.component';
import {MatIconModule, MatSidenavModule, MatToolbarModule} from '@angular/material';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

const appRoutes: Routes = [
  {path: '', component: DataComponent},
  {path: 'workflows', component: WorkflowsComponent},
  {path: 'about', component: AboutComponent},
  {path: '**', component: PageNotFoundComponent}
];

@NgModule({
  declarations: [
    AppComponent,
    DataComponent,
    AboutComponent,
    WorkflowsComponent,
    PageNotFoundComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    BrowserAnimationsModule,
    MatSidenavModule,
    MatToolbarModule,
    MatIconModule,
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
