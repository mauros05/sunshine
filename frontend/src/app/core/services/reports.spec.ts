import { TestBed } from '@angular/core/testing';

import { ReportsServie } from './reports.service';

describe('ReportsServie', () => {
  let service: ReportsServie;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReportsServie);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
