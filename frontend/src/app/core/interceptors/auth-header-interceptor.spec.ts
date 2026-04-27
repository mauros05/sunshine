import { TestBed } from '@angular/core/testing';
import { HttpHandlerFn, HttpRequest, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { authHeaderInterceptor } from './auth-header-interceptor';

describe('authHeaderInterceptor', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({});
    localStorage.clear();
  });

  function runInterceptor(initialRequest: HttpRequest<unknown>): HttpRequest<unknown> {
    let forwardedRequest: HttpRequest<unknown> | null = null;

    const next: HttpHandlerFn = (request) => {
      forwardedRequest = request;
      return of(new HttpResponse({ status: 200 }));
    };

    TestBed.runInInjectionContext(() => authHeaderInterceptor(initialRequest, next)).subscribe();

    if (!forwardedRequest) {
      throw new Error('Interceptor did not forward the request');
    }

    return forwardedRequest;
  }

  it('adds X-User-Id header when session contains userId', () => {
    localStorage.setItem('sunshine_user', JSON.stringify({ userId: 'abc-123' }));
    const request = new HttpRequest('GET', '/api/restaurants/1/orders');

    const forwarded = runInterceptor(request);

    expect(forwarded.headers.get('X-User-Id')).toBe('abc-123');
  });

  it('does not modify request when there is no session', () => {
    const request = new HttpRequest('GET', '/api/restaurants/1/orders');

    const forwarded = runInterceptor(request);

    expect(forwarded.headers.has('X-User-Id')).toBe(false);
  });

  it('does not modify request when session JSON is invalid', () => {
    localStorage.setItem('sunshine_user', '{invalid-json');
    const request = new HttpRequest('GET', '/api/restaurants/1/orders');

    const forwarded = runInterceptor(request);

    expect(forwarded.headers.has('X-User-Id')).toBe(false);
  });
});
