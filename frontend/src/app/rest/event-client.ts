import { RestClient } from './rest-client';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import {Event} from '../models/event';
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
    
    public cancelEvent(id: number): Observable<Event> {
        return super.put(
            (error: HttpErrorResponse) => {
                console.log(
                    'HTTP PATCH To Update Trainer With ID' +
                        id +
                        ' Failed: ' +
                        error.message
                );
            },
            '/',
            id
        );
    }

    public getEventById(id: number): Observable<Event>{
        return super.get((error: HttpErrorResponse) => {
            console.log(
                'HTTP Get Event with ID' + id + ' Failed: ' + error.message
            );
        }, '/' + id);
    }
}
