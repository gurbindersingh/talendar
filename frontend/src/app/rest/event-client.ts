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

    public postNewBirthday(event: Event): Observable<Event> {
        event.eventType = EventType.Birthday;
        event.endOfApplication = null;
        event.price = null;
        event.maxParticipant = null;
        event.description = null;
        event.minAge = null;
        event.maxAge = null;
        return super.post(
            (error: HttpErrorResponse) => {
                console.log('HTTP POST Birthday Failed: ' + error.message);
            },
            '',
            event
        );
    }
}