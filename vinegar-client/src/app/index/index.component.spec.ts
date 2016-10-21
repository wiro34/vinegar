/* tslint:disable:no-unused-variable */

import {TestBed, async} from '@angular/core/testing';
import {IndexComponent} from './index.component';

describe('Component: Index', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [IndexComponent],
    });
  });

  it('should create an instance', async(() => {
    let fixture = TestBed.createComponent(IndexComponent);
    let app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));

  it('should render title in a h* tag', async(() => {
      let fixture = TestBed.createComponent(IndexComponent);
      fixture.detectChanges();
      let compiled = fixture.debugElement.nativeElement;
      expect(compiled.querySelector('h1').textContent).toContain('a simple test tool for QA');
  }));

  it('should render sign-in button', async(() => {
      let fixture = TestBed.createComponent(IndexComponent);
      fixture.detectChanges();
      let compiled = fixture.debugElement.nativeElement;
      expect(compiled.querySelector('#sign-in-button')).toBeTruthy();
  }));
});
