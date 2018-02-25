import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';

import {routing} from './app.routing';
import {AppComponent} from './app.component';
import {DataService} from './services/data.service';
import {DataComponent} from './components/data/data.component';
import {AboutComponent} from './components/about/about.component';
import {WorkflowComponent} from './components/workflows/workflow.component';
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
import {ScatterSettingsComponent} from './components/data/chart/scatter/scatter-settings.component';
import {ColumnSelectorComponent} from './components/data/chart/shared/column-selector/column-selector.component';
import {DragDropDirectiveModule} from 'angular4-drag-drop';
import {ContextMenuModule} from 'angular4-contextmenu/angular2-contextmenu';
import { ContextMenuService } from 'angular4-contextmenu/src/contextMenu.service';
import {LinearRegressionBlockComponent} from './components/workflows/blocks/machine-learning/regression/linear-regression-block.component';
// noinspection TsLint
import {PoissonRegressionBlockComponent} from './components/workflows/blocks/machine-learning/regression/poisson-regression-block.component';
import {ClickOutsideModule} from 'ng-click-outside';
import { NewColumnSettingsComponent } from './components/data/new-column-settings/new-column-settings.component';
import { ProcessFeatureComponent } from './components/data/process-feature/process-feature.component';
import {WorkflowDirective} from './components/workflows/workflow.directive';
import {DatabaseBlockComponent} from './components/workflows/blocks/import-data/database-block.component';
import {FileBlockComponent} from './components/workflows/blocks/import-data/file-block.component';
import {BlockService} from './services/block.service';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    FooterComponent,
    DataComponent,
    AboutComponent,
    WorkflowComponent,
    SidebarComponent,
    PageNotFoundComponent,
    DatabaseBlockComponent,
    FileBlockComponent,
    LinearRegressionBlockComponent,
    PoissonRegressionBlockComponent,
    ChartComponent,
    HistogramSettingsComponent,
    PieSettingsComponent,
    ScatterSettingsComponent,
    ColumnSelectorComponent,
    NewColumnSettingsComponent,
    ProcessFeatureComponent,
    WorkflowDirective
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    InfiniteScrollModule,
    DragDropDirectiveModule,
    ContextMenuModule,
    ClickOutsideModule,
    translate,
    logging,
    routing
  ],
  providers: [
    DataService,
    BlockService,
    ContextMenuService
  ],
  entryComponents: [
    DatabaseBlockComponent,
    FileBlockComponent,
    LinearRegressionBlockComponent,
    PoissonRegressionBlockComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
