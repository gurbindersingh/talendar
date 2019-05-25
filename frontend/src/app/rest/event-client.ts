import { RestClient } from './rest-client';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import {Event, EventType} from '../models/event';
import { Observable } from 'rxjs';

@Injectable()
export class EventClient extends RestClient {
    constructor(httpClient: HttpClient) {
        super('events', httpClient);
    }

    public postNewEvent(event: Event): Observable<Event> {
        console.log(JSON.stringify(event));
        return super.post(
            (error: HttpErrorResponse) => {
                console.log('HTTP POST Birthday Failed: ' + error.message);
            },
            '',
            event
        );
    }
}
