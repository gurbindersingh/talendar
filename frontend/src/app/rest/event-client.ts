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
        return super.delete((error: HttpErrorResponse) => {
            console.log(
                'HTTP Delete To Cacnel Event With ID' +
                    id +
                    ' Failed: ' +
                    error.message
            );
        }, '/' + id);
    }

    public getAllEvents(): Observable<Event[]> {
        return super.get((error: HttpErrorResponse) => {
            console.log('HTTP GET All Events Failed: ' + error.message);
        }, '/all');
    }

    public getAllFutureCourses(): Observable<Event[]> {
        return super.get((error: HttpErrorResponse) => {
            console.log('HTTP GET All Future Courses Failed ' + error.message);
        }, '');
    }

    public getEventById(id: number): Observable<Event> {
        return super.get((error: HttpErrorResponse) => {
            console.log(
                'HTTP Get Event with ID' + id + ' Failed: ' + error.message
            );
        }, '/' + id);
    }

    public update(event: Event): Observable<Event> {
        return super.put(
            (error: HttpErrorResponse) => {
                console.log(
                    'HTTP PATCH To Update Event With ID' +
                        event.id +
                        ' Failed: ' +
                        error.message
                );
            },
            '',
            event
        );
    }

    public updateCustomer(event: Event): Observable<Event> {
        return super.put(
            (error: HttpErrorResponse) => {
                console.log(
                    'HTTP PATCH To Update Event With ID' +
                        event.id +
                        ' Failed: ' +
                        error.message
                );
            },
            '/customers',
            event
        );
    }
}
