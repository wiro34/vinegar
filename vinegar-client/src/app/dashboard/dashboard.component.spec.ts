/* tslint:disable:no-unused-variable */

import { TestBed, async } from '@angular/core/testing';
import { DashboardComponent } from './dashboard.component';

describe('Component: Dashboard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DashboardComponent],
    });
  });

  it('should create an instance', async(() => {
    let fixture = TestBed.createComponent(DashboardComponent);
    let app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));
});
