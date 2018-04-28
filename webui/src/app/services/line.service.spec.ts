import {LineService} from './line.service';
import {TestBed} from '@angular/core/testing';
import {DialogService} from 'ng2-bootstrap-modal';
import {BlockService} from './block.service';
import {Tuple} from '../util/tuple';
import {BlockComponentsRelation} from '../components/workflows/blocks/block-components-relation';
import {FileBlockComponent} from '../components/workflows/blocks/import-data/file-block.component';
import {WireType} from '../components/workflows/blocks/wire-type';
import {LinearRegressionBlockComponent} from '../components/workflows/blocks/machine-learning/regression/linear-regression-block.component';
import {PoissonRegressionBlockComponent} from '../components/workflows/blocks/machine-learning/regression/poisson-regression-block.component';

describe('Service: LineService', () => {
  let lineService: LineService;
  let blockService: BlockService;
  let dialogService: DialogService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        LineService,
        {provide: BlockService, useValue: new BlockServiceStub()},
        DialogService
      ]
    });
  });

  beforeEach(() => {
    lineService = TestBed.get(LineService);
    blockService = TestBed.get(BlockService);
    dialogService = TestBed.get(DialogService);

    spyOn(document, 'getElementById').and.callFake(() => {
      return document.createElement('p');
    });
  });

  it('should create service', () => {
    expect(lineService).not.toBeNull();
    expect(lineService.componentsMap).toEqual(new Map<Tuple<string, string>, Array<BlockComponentsRelation>>());
    expect(lineService.startComponent).toBeUndefined();
  });

  it('should not do anything if a start point is already selected', () => {
    // given
    lineService.startId = 'testStart';

    // when
    lineService.startLine(new FileBlockComponent(blockService, dialogService), 'fileComponent', WireType.DATASET, 0);

    // then
    expect(lineService.startId).toBe('testStart');
    expect(lineService.componentsMap.size).toBe(0);
  });

  it('should set start point', () => {
    // given
    const file = new FileBlockComponent(blockService, dialogService);

    // when
    lineService.startLine(file, 'fileComponent', WireType.DATASET, 0);

    // then
    expect(document.getElementById).toHaveBeenCalledWith('fileComponent');
    expect(lineService.startComponent).toEqual(file);
    expect(lineService.startId).toBe('fileComponent');
    expect(lineService.outputType).toBe(WireType.DATASET);
    expect(lineService.componentsMap.size).toBe(0);
  });

  it ('should deselect start point', () => {
    // given
    const file = new FileBlockComponent(blockService, dialogService);
    lineService.startLine(file, 'fileComponent', WireType.DATASET, 0);

    // when
    lineService.deselectLineStartPoint('fileComponent');

    // then
    expect(lineService.startId).toBeNull();
    expect(lineService.startComponent).toBeNull();
    expect(lineService.outputType).toBeNull();
    expect(lineService.outputIndex).toBeNull();
  });

  it('should not do anything if the end point is not valid', () => {
    // given
    const lRegression = new LinearRegressionBlockComponent(blockService, dialogService);
    const pRegression = new PoissonRegressionBlockComponent(blockService, dialogService);
    lineService.startLine(lRegression, 'lRegressionComponent', WireType.REGRESSION_MODEL, 0);

    // when
    lineService.endLine(pRegression, 'pRegressionComponent', WireType.DATASET, 0);

    // then
    expect(lineService.componentsMap.size).toBe(0);
  });

  it('should draw the end line', () => {
    // given
    const file = new FileBlockComponent(blockService, dialogService);
    const lRegression = new LinearRegressionBlockComponent(blockService, dialogService);
    lineService.startLine(file, 'fileComponent', WireType.DATASET, 0);

    // when
    lineService.endLine(lRegression, 'lRegressionComponent', WireType.DATASET, 0);

    // then
    const key = new Tuple<string, string>(file.id, lRegression.id);

    // expect(lineService.componentsMap.size).toBe(1);
    // expect(lineService.componentsMap.keys().next).toEqual(key);
    // expect(lineService.componentsMap.get(key).length).toEqual(1);

    // const blockRelation: BlockComponentsRelation = lineService.componentsMap.get(key)[0];
    // expect(blockRelation.component1).toEqual(file);
    // expect(blockRelation.component2).toEqual(lRegression);
  });
});

class BlockServiceStub {

}
