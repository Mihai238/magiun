import {ModelSelectionComponent} from "./model-selection.component";
import {async, ComponentFixture, getTestBed, TestBed} from "@angular/core/testing";
import {DataService} from "../../services/data.service";
import {RecommenderRestService} from "../../rest/recommender.rest.service";
import {TranslateService} from "@ngx-translate/core";
import {DialogService} from 'ng2-bootstrap-modal';
import {DistributionsModalComponent} from "./distributions-modal/distributions-modal.component";
import {translate} from '../../app.translate';
import {logging} from '../../app.logging';
import {HttpClientModule} from "@angular/common/http";
import {RouterTestingModule} from "@angular/router/testing";
import {Column, ColumnType, DataSet, Schema} from "../../model/data-set.model";
import {Observable} from "rxjs";
import {NavigationEnd, Router} from "@angular/router";
import {VarDirective} from "../../directives/var.directive";

describe("ModelSelectionComponent", () => {

  let component: ModelSelectionComponent;
  let fixture: ComponentFixture<ModelSelectionComponent>;
  let dataService: DataService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        ModelSelectionComponent,
        DistributionsModalComponent,
        VarDirective
      ],
      imports: [
        HttpClientModule,
        translate,
        logging
      ],
      providers: [
        DataService,
        RecommenderRestService,
        DialogService,
        TranslateService,
        [{provide: Router, useClass: RouterMock}]
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    dataService = TestBed.get(DataService);
    spyOn(dataService, "getDataSets").and.returnValue(Observable.of(datasets));
    spyOn(dataService, "getDistributions").and.returnValue(Observable.of(new Map()));

    fixture = TestBed.createComponent(ModelSelectionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the variables after receiving the datasets list', () => {

    // given

    // when

    // then
    expect(component.selectedDataset).toBe(dataset1);
    expect(component.targetVariable).toBe(columnName);
    expect(component.explanatoryVariables).toEqual([]);
    expect(component.possibleExplanatoryVariables).toEqual([columnAge, columnMarried]);
  });

  it('should not update the selected dataset and the rest of the variables when receiving null as a index', () => {
    // given

    // when
    component.updateSelectedDataset(null);

    // then
    expect(component.selectedDataset).toBe(dataset1);
    expect(component.targetVariable).toBe(columnName);
    expect(component.explanatoryVariables).toEqual([]);
    expect(component.possibleExplanatoryVariables).toEqual([columnAge, columnMarried]);
  });

  it('should not update the selected dataset and the rest of the variables when receiving index < 0', () => {
    // given

    // when
    component.updateSelectedDataset(-3);

    // then
    expect(component.selectedDataset).toBe(dataset1);
    expect(component.targetVariable).toBe(columnName);
    expect(component.explanatoryVariables).toEqual([]);
    expect(component.possibleExplanatoryVariables).toEqual([columnAge, columnMarried]);
  });

  it('should update the selected dataset and the rest of the variables', () => {
    // given

    // when
    component.updateSelectedDataset(2);

    // then
    expect(component.selectedDataset).toBe(dataset3);
    expect(component.targetVariable).toBe(columnName);
    expect(component.explanatoryVariables).toEqual([]);
    expect(component.possibleExplanatoryVariables).toEqual([columnSex, columnMarried, columnAge, columnUUID]);
  });

  it('should update the selected target variable and the possible explanatory variables', () => {
    // given

    // when
    component.updateTargetVariable(2);

    // then
    expect(component.selectedDataset).toBe(dataset1);
    expect(component.targetVariable).toBe(columnMarried);
    expect(component.explanatoryVariables).toEqual([]);
    expect(component.possibleExplanatoryVariables).toEqual([columnName, columnAge]);
  });

});


export const columnName: Column = new Column(0, "name", ColumnType.String);
export const columnAge: Column = new Column(1, "age", ColumnType.Double);
export const columnMarried: Column = new Column(2, "married", ColumnType.Boolean);
export const columnSex: Column = new Column(3, "sex", ColumnType.Int);
export const columnUUID: Column = new Column(4, "uuid", ColumnType.Unknown);

export const schema1: Schema = new Schema([columnName, columnAge, columnMarried], 3);
export const dataset1: DataSet = new DataSet(1, 'dataset1', schema1);

export const schema2: Schema = new Schema([columnSex, columnMarried, columnAge, columnUUID], 2);
export const dataset2: DataSet = new DataSet(2, 'dataset2', schema2);

export const schema3: Schema = new Schema([columnName, columnSex, columnMarried, columnAge, columnUUID], 5);
export const dataset3: DataSet = new DataSet(3, 'dataset3', schema3);

export const datasets: DataSet[] = [dataset1, dataset2, dataset3];

export class RouterMock {
  events = Observable.of(new NavigationEnd(1, '/model-selection', '/model-selection'));
}
