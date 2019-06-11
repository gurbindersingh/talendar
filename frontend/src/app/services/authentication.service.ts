import { Injectable } from '@angular/core';
import { AuthenticationClient } from '../rest/authentication-client';
import { SessionStorageService } from './session-storage.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { UserDetails } from '../models/user-details';
import { NotificationService } from './notification.service';

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
            console.log(this.sessionStorageService.sessionToken);
            return this.authenticationClient.userDetails(
                this.sessionStorageService.sessionToken
            );
        }
    }

    login(email: string, password: string): Observable<void> {
        return this.authenticationClient
            .authenticate({
                email,
                password,
            })
            .pipe(
                map((response) => {
                    this.sessionStorageService.setLoggedIn(response);
                    this.notificationService.notifyLogin();
                })
            );
    }

    logout(): void {
        this.sessionStorageService.setLoggedIn(false);
        this.notificationService.notifyLogout();
    }
}
