import { Injectable } from '@angular/core';
import { AuthenticationClient } from '../rest';
import { SessionStorageService } from './session-storage.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserDetails } from '../models/user-details';
import { NotificationService } from './notification.service';
import { SHA3 } from 'sha3';
import { HttpHeaders } from '@angular/common/http';

@Injectable({
    providedIn: 'root',
})
export class AuthenticationService {
    constructor(
        private authenticationClient: AuthenticationClient,
        private sessionStorageService: SessionStorageService,
        private notificationService: NotificationService
    ) {}

    get isLoggedIn(): boolean {
        return this.sessionStorageService.loggedIn;
    }

    getUserDetails(): Observable<UserDetails> {
        if (this.sessionStorageService.loggedIn === false) {
            return null;
        } else {
            
            return this.authenticationClient.userDetails(
                this.sessionStorageService.sessionToken
            );
        }
    }

    login(email: string, password: string): Observable<void> {
        console.warn(new SHA3(512).update(password).digest('hex'));

        return this.authenticationClient
            .authenticate({
                email,
                // password,
                password: new SHA3(512).update(password).digest('hex'),
            })
            .pipe(
                map((response) => {
                    this.sessionStorageService.setLoggedIn(response);
                    this.notificationService.notifyLogin();
                })
            );
    }

    renewLogin(header: HttpHeaders): Observable<void> {
        return this.authenticationClient.renewAuthentication(header).pipe(
            map((response) => {
                this.sessionStorageService.setLoggedIn(response);
            })
        );
    }

    logout(): void {
        this.sessionStorageService.setLoggedIn(false);
        this.notificationService.notifyLogout();
    }
}
