import {BlockService} from './block.service';
import {async, TestBed} from '@angular/core/testing';
import {HttpClientModule} from '@angular/common/http';
import {BlockController} from '../controllers/block.controller';
import {LineService} from './line.service';
import {DialogService} from 'ng2-bootstrap-modal';
import {FileBlockComponent} from '../components/workflows/blocks/import-data/file-block.component';
import {DragDropDirectiveModule} from 'angular4-drag-drop';
import {translate} from '../app.translate';
import {CollectionsUtils} from '../util/collections.utils';
import {LinearRegressionBlockComponent} from '../components/workflows/blocks/machine-learning/regression/linear-regression-block.component';
import {WireType} from '../components/workflows/blocks/wire-type';
import {Tuple} from '../util/tuple';

fdescribe('Service: BlockService', () => {
  let blockService: BlockService;
  let lineServie: LineService;
  let blockController: BlockController;
  let dialogService: DialogService;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [
        FileBlockComponent
      ],
      imports: [
        HttpClientModule,
        DragDropDirectiveModule,
        translate
      ],
      providers: [
        BlockController,
        LineService,
        BlockService,
        DialogService
      ]
    });
  }));

  beforeEach(() => {
    blockService = TestBed.get(BlockService);
    lineServie = TestBed.get(LineService);
    blockController = TestBed.get(BlockController);
    dialogService = TestBed.get(DialogService);
    blockService.blocks.clear();
  });

  it('service should be created!', () => {
    expect(blockService).not.toBeNull();
    expect(blockService.blocks.size).toBe(0);
  });


  it('should add a block', () => {
    // given
    spyOn(blockController, 'upsertBlock').and.callFake(() => {});

    // when
    const fileBlockComponent = new FileBlockComponent(blockService, dialogService);
    blockService.addBlock(fileBlockComponent);

    // then
    expect(blockService.blocks.size).toBe(1);
    expect(CollectionsUtils.getMapKeyByIndex(blockService.blocks, 0)).toBe(fileBlockComponent.id);
  });

  it('should remove block', () => {
    // given
    spyOn(blockController, 'upsertBlock').and.callFake(() => {});
    spyOn(lineServie, 'deleteComponent').and.callFake(() => {});
    spyOn(blockController, 'deleteBlock').and.callFake(() => {});

    const fileBlockComponent = new FileBlockComponent(blockService, dialogService);

    blockService.addBlock(fileBlockComponent);

    // when
    blockService.deleteBlock(fileBlockComponent);

    // then
    expect(blockService.blocks.size).toBe(0);
  });

  // TODO: fixme
  it('should add block as input into the input array after line was draw', () => {
    // given
    const fileBlockComponent = new FileBlockComponent(blockService, dialogService);
    let regressionBlock = new LinearRegressionBlockComponent(blockService, dialogService);

    blockService.addBlock(fileBlockComponent);
    blockService.addBlock(regressionBlock);

    spyOn(blockController, 'upsertBlock').and.callFake(() => {});
    spyOn(lineServie, 'endLine').and.returnValue(new Tuple<string, number>(fileBlockComponent.id, 0));

    // when
    blockService.endLine(regressionBlock, 'end', WireType.DATASET, 0);

    // then
    regressionBlock = CollectionsUtils.getMapValueByIndex(blockService.blocks, 1);

    expect(regressionBlock.setInputs.length).toBe(1);
    expect(regressionBlock.setInputs[0]).toEqual(new Tuple<string, number>(fileBlockComponent.id, 0));
  });

  // TODO: fixme
  it('should delete block also from the inputs of another block', () => {
    // given
    spyOn(blockController, 'upsertBlock').and.callFake(() => {});
    spyOn(lineServie, 'deleteComponent').and.callFake(() => {});
    spyOn(blockController, 'deleteBlock').and.callFake(() => {});

    const fileBlockComponent = new FileBlockComponent(blockService, dialogService);
    let regressionBlock = new LinearRegressionBlockComponent(blockService, dialogService);
    regressionBlock.setInputs.push(new Tuple<string, number>(fileBlockComponent.id, 0));

    blockService.addBlock(fileBlockComponent);
    blockService.addBlock(regressionBlock);

    // when
    blockService.deleteBlock(fileBlockComponent);

    // then
    expect(blockService.blocks.size).toBe(1);
    regressionBlock = CollectionsUtils.getMapValueByIndex(blockService.blocks, 0);
    expect(regressionBlock.setInputs).toEqual([]);
  });

});
