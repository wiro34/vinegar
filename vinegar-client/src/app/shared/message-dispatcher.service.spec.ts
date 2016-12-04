/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { MessageDispatcher } from './message-dispatcher.service';

describe('Service: MessageDispatcher', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MessageDispatcher]
    });
  });

  it('should ...', inject([MessageDispatcher], (service: MessageDispatcher) => {
    expect(service).toBeTruthy();
  }));
});
