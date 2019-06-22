import { Injectable } from "@angular/core";
import { RestClient } from './rest-client';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Customer } from './../models/customer';

@Injectable()
export class CustomerClient extends RestClient {
    constructor(httpClient: HttpClient) {
        super('customers', httpClient);
    }

    public update(email: string): Observable<Customer> {
        return super.put(
            (error: HttpErrorResponse) => {
                console.log(
                    'HTTP PATCH To Opt-Out Customer With E-Mail: ' +
                    email +
                    ' Failed: ' +
                    error.message
                );
            },
            '',
            email
        );
    }
}