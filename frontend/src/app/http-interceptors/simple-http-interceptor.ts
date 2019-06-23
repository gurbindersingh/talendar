import {
    HttpRequest,
    HttpInterceptor,
    HttpHandler,
    HttpEvent,
    HttpErrorResponse,
    HttpHeaders,
} from '@angular/common/http';
import { Observable, EMPTY, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { SessionStorageService } from '../services/session-storage.service';
import { Injectable } from '@angular/core';
import { AuthenticationService } from '../services/authentication.service';
import { NotificationService } from '../services/notification.service';
import { Router } from '@angular/router';

@Injectable()
export class SimpleHttpInterceptor implements HttpInterceptor {
    constructor(
        private sessionService: SessionStorageService,
        private authenticationService: AuthenticationService,
        private router: Router
    ) {}

    private doRenew = true;

    intercept(
        req: HttpRequest<any>,
        next: HttpHandler
    ): Observable<HttpEvent<any>> {
        {
            const token = this.sessionService.sessionToken;

            const authReq = req.clone({
                headers: req.headers.set('Authorization', 'Bearer ' + token),
            });

            const headers: HttpHeaders = authReq.headers;

            if (
                this.sessionService.loggedIn &&
                this.sessionService.isOldToken &&
                this.doRenew
            ) {
                this.doRenew = false;

                this.authenticationService.renewLogin(headers).subscribe(
                    (data) => {
                        this.doRenew = true;
                    },
                    (error: Error) => {
                        console.log(
                            'Authentication could not be renewed: ' +
                                error.message
                        );
                        this.doRenew = true;
                    }
                );
            }

            // check if token is expired, if yes, logout (which will notigy logout action)
            if (token != null) {
                const parsedToken = JSON.parse(atob(token.split('.')[1]));
                if (parsedToken.exp < Date.now() / 1000) {
                    this.authenticationService.logout();
                }
            }

            return next.handle(authReq).pipe(
                catchError((error: HttpErrorResponse, caught) => {
                    if (error.status === 401) {
                        this.router.navigateByUrl('/login');
                    }
                    throw error;
                })
            );
        }
    }
}
