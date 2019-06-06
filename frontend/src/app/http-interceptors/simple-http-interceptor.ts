import {
    HttpRequest,
    HttpInterceptor,
    HttpHandler,
    HttpEvent,
    HttpErrorResponse,
} from '@angular/common/http';
import { Observable, EMPTY, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { SessionStorageService } from '../services/session-storage-service';
import { Injectable } from '@angular/core';

@Injectable()
export class SimpleHttpInterceptor implements HttpInterceptor {
    constructor(private sessionService: SessionStorageService) {}

    intercept(
        req: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        {
            const token = this.sessionService.sessionToken;
            const authReq = req.clone({
                headers: req.headers.set('Authorization', 'Bearer ' + token),
            });

            console.log('Interceptor: Token = ' + token);

            return next.handle(authReq).pipe(
                catchError((error: HttpErrorResponse, caught) => {
                    if (error.status === 401) {
                        location.pathname = '/login';
                    }
                    throw error;
                })
            );
            /*catch((error, caught) => {
                return Observable.throw(error);
            }) as any; */
        }
    }
}
