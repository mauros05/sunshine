import { HttpInterceptorFn } from '@angular/common/http';

export const authHeaderInterceptor: HttpInterceptorFn = (req, next) => {
  const raw = localStorage.getItem('sunshine_user');

  if (!raw) {
    return next(req);
  }

  try {
    const user = JSON.parse(raw);
    const userId = user?.userId;

    if (!userId) {
      return next(req);
    }

    const cloned = req.clone({
      setHeaders: {
        'X-User-Id': userId
      }
    });

    return next(cloned);
  } catch {
    return next(req);
  }
};
