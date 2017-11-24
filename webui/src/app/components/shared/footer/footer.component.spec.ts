import {async, ComponentFixture, ComponentFixtureAutoDetect, TestBed} from '@angular/core/testing';
import {FooterComponent} from './footer.component';
import {DebugElement} from '@angular/core';
import {By} from '@angular/platform-browser';
import {TranslateModule} from '@ngx-translate/core';

describe('FooterComponent', () => {

  let comp: FooterComponent;
  let fixture: ComponentFixture<FooterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [FooterComponent],
      imports: [TranslateModule.forRoot()],
      providers: [
        {provide: ComponentFixtureAutoDetect, useValue: true}
      ]
    })
      .compileComponents();

  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FooterComponent);
    comp = fixture.componentInstance;
  });

  it('list includes 3 elements', () => {
    const debugElems: DebugElement[] = fixture.debugElement.queryAll(By.css('li'));

    expect(debugElems.length).toBe(3);
  });

  it('about entry is present', () => {
    const debugElem: DebugElement = fixture.debugElement.query(By.css('li > a'));
    const elem: HTMLElement = debugElem.nativeElement;

    expect(elem.textContent).toContain('FOOTER.ABOUT');
  });
});
