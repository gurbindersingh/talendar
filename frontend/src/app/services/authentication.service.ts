import { Injectable } from '@angular/core';
import { AuthenticationClient } from '../rest/authentication-client';
import { SessionStorageService } from './session-storage-service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';


@Injectable({
    providedIn: 'root',
})
export class AuthenticationService {
    constructor(
        private authenticationClient: AuthenticationClient,
        private sessionStorageService: SessionStorageService
    ) {}

    get isLoggedIn(): boolean {
        return this.sessionStorageService.loggedIn;
    }

    login(email: string, password: string): Observable<void> {
        return this.authenticationClient
            .authenticate({
                email,
                password,
            })
            .pipe(
                map((response) =>
                    this.sessionStorageService.setLoggedIn(response)
                )
            );
    }

    logout(): void {
        this.sessionStorageService.setLoggedIn(false);
    }
}
