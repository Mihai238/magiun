import {ComponentFixture, TestBed} from '@angular/core/testing';
import {FooterComponent} from './footer.component';
import {translate} from '../../../app.translate';
import {DebugElement} from '@angular/core';
import {By} from '@angular/platform-browser';

describe('FooterComponent', () => {

  let comp: FooterComponent;
  let fixture: ComponentFixture<FooterComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FooterComponent],
      imports: [translate]
    });

    fixture = TestBed.createComponent(FooterComponent);
    comp = fixture.componentInstance;
  });

  it('true is true', () => {
    const debugElems: DebugElement[] = fixture.debugElement.queryAll(By.css('li'));

    expect(debugElems.length).toBe(3);
  });
});
