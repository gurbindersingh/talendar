import { RestClient } from './rest-client';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Event } from '../models/event';
import { Observable } from 'rxjs';

@Injectable()
export class EventClient extends RestClient {
    constructor(httpClient: HttpClient) {
        super('events', httpClient);
    }

    public postNewEvent(event: Event): Observable<Event> {
        return super.post(
            (error: HttpErrorResponse) => {
                console.log('HTTP POST Event Failed: ' + error.message);
            },
            '',
            event
        );
    }
}