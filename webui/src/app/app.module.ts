import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';

import {routing} from './app.routing';
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
import {HttpClientModule} from '@angular/common/http';
import {translate} from './app.translate';
import {SidebarComponent} from './components/workflows/sidebar/sidebar.component';
import {ChartComponent} from './components/data/chart/chart.component';
import {PieSettingsComponent} from './components/data/chart/pie/pie-settings.component';
import {HistogramSettingsComponent} from './components/data/chart/histogram/histogram-settings.component';
import { ScatterSettingsComponent } from './components/data/chart/scatter/scatter-settings.component';


@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    DataComponent,
    AboutComponent,
    WorkflowsComponent,
    SidebarComponent,
    PageNotFoundComponent,
    ChartComponent,
    HistogramSettingsComponent,
    PieSettingsComponent,
    ScatterSettingsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    InfiniteScrollModule,
    translate,
    logging,
    routing
  ],
  providers: [
    DataService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
