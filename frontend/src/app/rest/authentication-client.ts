import { RestClient } from './rest-client';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthenticationRequest } from '../models/authentication-request';
import { AuthenticationResponse } from '../models/authentication-response';
import { UserDetails } from '../models/user-details';

@Injectable()
export class AuthenticationClient extends RestClient {
    constructor(httpClient: HttpClient) {
        super('authentication', httpClient);
    }

    public authenticate(
        authenticationData: AuthenticationRequest
    ): Observable<AuthenticationResponse> {
        return this.post(
            (error: HttpErrorResponse) => {
                console.log(error.message);
            },
            '',
            authenticationData
        );
    }

    public userDetails(token: string): Observable<UserDetails> {
        return this.get((error: HttpErrorResponse) => {
            console.log(error.message);
        }, '/info/' + token);
    }
}
