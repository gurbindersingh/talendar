import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { SimpleHttpInterceptor } from './simple-http-interceptor';

/** Http interceptor providers in outside-in order */
export const httpInterceptorProviders = [
    {
        provide: HTTP_INTERCEPTORS,
        useClass: SimpleHttpInterceptor,
        multi: true,
    },
];
