import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';

import {routing} from './app.routing';
import {translate} from './app.translate';
import {AppComponent} from './app.component';
import {DataService} from './services/data.service';
import {DataComponent} from './components/data/data.component';
import {AboutComponent} from './components/about/about.component';
import {WorkflowsComponent} from './components/workflows/workflows.component';
import {PageNotFoundComponent} from './components/page-not-found/page-not-found.component';
import {NavbarComponent} from './components/shared/navbar/navbar.component';
import {InfiniteScrollModule} from 'ngx-infinite-scroll';
import {FooterComponent} from './components/shared/footer/footer.component';
import {logging} from './app.logging';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    DataComponent,
    AboutComponent,
    WorkflowsComponent,
    PageNotFoundComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule,
    InfiniteScrollModule,
    logging,
    translate,
    routing
  ],
  providers: [
    DataService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
