import {CommonModule} from '@angular/common';
import {BrowserModule} from '@angular/platform-browser';
import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {RouteReuseStrategy} from '@angular/router';
import {DragDropDirectiveModule} from 'angular4-drag-drop';
import {ClickOutsideModule} from 'ng-click-outside';
import {BootstrapModalModule} from 'ng2-bootstrap-modal';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {TagInputModule} from 'ngx-chips';

import {routing} from './app.routing';
import {AppComponent} from './app.component';
import {DataService} from './services/data.service';
import {DataComponent} from './components/data/data.component';
import {AboutComponent} from './components/about/about.component';
import {WorkflowComponent} from './components/workflows/workflow.component';
import {PageNotFoundComponent} from './components/page-not-found/page-not-found.component';
import {NavbarComponent} from './components/shared/navbar/navbar.component';
import {FooterComponent} from './components/shared/footer/footer.component';
import {logging} from './app.logging';
import {translate} from './app.translate';
import {SidebarComponent} from './components/workflows/sidebar/sidebar.component';
import {ChartComponent} from './components/data/chart/chart.component';
import {PieSettingsComponent} from './components/data/chart/pie/pie-settings.component';
import {HistogramSettingsComponent} from './components/data/chart/histogram/histogram-settings.component';
import {ScatterSettingsComponent} from './components/data/chart/scatter/scatter-settings.component';
import {ColumnSelectorComponent} from './components/data/chart/shared/column-selector/column-selector.component';
import {LinearRegressionBlockComponent} from './components/workflows/blocks/machine-learning/regression/linear-regression-block.component';
import {PoissonRegressionBlockComponent} from './components/workflows/blocks/machine-learning/regression/poisson-regression-block.component';
import { AddColumnSettingsComponent } from './components/data/new-column-settings/add-column-settings.component';
import { EditColumnComponent } from './components/data/process-feature/edit-column.component';
import {WorkflowDirective} from './directives/workflow.directive';
import {DatabaseBlockComponent} from './components/workflows/blocks/import-data/database-block.component';
import {FileBlockComponent} from './components/workflows/blocks/import-data/file-block.component';
import {BlockService} from './services/block.service';
import {ParametersModalComponent} from './components/workflows/blocks/parameters-modal/parameters-modal.component';
import {RoutingReuseStrategy} from './app.routing.reuse.strategy';
import {SplitDataBlockComponent} from './components/workflows/blocks/data-transformation/split-data-block.component';
import {FileParameterComponent} from './components/workflows/blocks/parameters-modal/parameters/file/file-parameter.component';
import {SelectParameterComponent} from './components/workflows/blocks/parameters-modal/parameters/select/select-parameter.component';
import {InputParameterComponent} from './components/workflows/blocks/parameters-modal/parameters/input/input-parameter.component';
import {CheckboxParameterComponent} from './components/workflows/blocks/parameters-modal/parameters/checkbox/checkbox-parameter.component';
import {DataTableModule} from './components/shared/table';
import {DropColumnsBlockComponent} from './components/workflows/blocks/feature-selection/drop-columns-block.component';
import {MultiInputParameterComponent} from './components/workflows/blocks/parameters-modal/parameters/multi-input/multi-input-parameter.component';
import {LineService} from './services/line.service';
import {BlockRestService} from './rest/block.rest.service';
import { BarSettingsComponent } from './components/data/chart/bar-settings/bar-settings.component';
import {ExecutionService} from "./services/execution.service";
import {ModelSelectionComponent} from "./components/model-selection/model-selection.component";
import {AlgorithmRestService} from "./rest/algorithm-rest.service";
import {DistributionsModalComponent} from "./components/model-selection/distributions-modal/distributions-modal.component";
import {PlotsModalComponent} from "./components/model-selection/plots-modal/plots-modal.component";
import {VarDirective} from "./directives/var.directive";
import {LoadingIndicatorService} from "./services/loading.indicator.service";
import {LoadingIndicatorInterceptor} from "./interceptor/loading.indicator.interceptor";
import {HttpLoaderComponent} from "./components/shared/http-loader/http-loader.component";

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
    BarSettingsComponent,
    ColumnSelectorComponent,
    AddColumnSettingsComponent,
    EditColumnComponent,
    WorkflowDirective,
    ParametersModalComponent,
    SplitDataBlockComponent,
    DropColumnsBlockComponent,
    FileParameterComponent,
    SelectParameterComponent,
    InputParameterComponent,
    MultiInputParameterComponent,
    CheckboxParameterComponent,
    ModelSelectionComponent,
    DistributionsModalComponent,
    PlotsModalComponent,
    VarDirective,
    HttpLoaderComponent
  ],
  imports: [
    CommonModule,
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    DragDropDirectiveModule,
    ClickOutsideModule,
    BootstrapModalModule,
    DataTableModule,
    BrowserAnimationsModule,
    TagInputModule,
    translate,
    logging,
    routing
  ],
  providers: [
    DataService,
    BlockService,
    LineService,
    BlockRestService,
    ExecutionService,
    AlgorithmRestService,
    {provide: RouteReuseStrategy, useClass: RoutingReuseStrategy},
    LoadingIndicatorService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LoadingIndicatorInterceptor,
      multi: true,
      deps: [LoadingIndicatorService]
    }
  ],
  entryComponents: [
    DatabaseBlockComponent,
    FileBlockComponent,
    LinearRegressionBlockComponent,
    PoissonRegressionBlockComponent,
    ParametersModalComponent,
    SplitDataBlockComponent,
    DropColumnsBlockComponent,
    FileParameterComponent,
    SelectParameterComponent,
    InputParameterComponent,
    CheckboxParameterComponent,
    DistributionsModalComponent,
    PlotsModalComponent
  ],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
  bootstrap: [AppComponent]
})
export class AppModule {
}
