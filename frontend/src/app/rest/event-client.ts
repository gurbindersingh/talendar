import { RestClient } from './rest-client';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Event } from '../models/event';
import { Observable } from 'rxjs';
import { EventType } from '../models/enum/eventType';

@Injectable()
export class EventClient extends RestClient {
    constructor(httpClient: HttpClient) {
        super('events', httpClient);
    }

    public postNewEvent(event: Event): Observable<Event> {
        let eventSpecificEndpoint: string;

        switch (event.eventType) {
            case EventType.Birthday:
                eventSpecificEndpoint = '/birthday';
                break;
            case EventType.Rent:
                eventSpecificEndpoint = '/rent';
                break;
            case EventType.Consultation:
                eventSpecificEndpoint = '/consultation';
                break;
            case EventType.Course:
                eventSpecificEndpoint = '/course';
                break;
        }

        console.log(JSON.stringify(event));
        return super.post(
            (error: HttpErrorResponse) => {
                console.log('HTTP POST Birthday Failed: ' + error.message);
            },
            eventSpecificEndpoint,
            event
        );
    }

    public cancelEvent(id: number): Observable<Event> {
        return super.delete((error: HttpErrorResponse) => {
            console.log(
                'HTTP Delete To Cancel Event With ID' +
                    id +
                    ' Failed: ' +
                    error.message
            );
        }, '/' + id);
    }

    public getAllEvents_adminView(): Observable<Event[]> {
        return super.get((error: HttpErrorResponse) => {
            console.log(
                'HTTP GET All Events (For Admin) Failed: ' + error.message
            );
        }, '/all/admin');
    }

    public getAllEvents_trainerView(id: number): Observable<Event[]> {
        return super.get((error: HttpErrorResponse) => {
            console.log(
                'HTTP GET All Events (For trainer) Failed: ' + error.message
            );
        }, '/all/trainer/' + id);
    }

    public getAllEvents_clientView(): Observable<Event[]> {
        return super.get((error: HttpErrorResponse) => {
            console.log(
                'HTTP GET All Events (For Clients) Failed: ' + error.message
            );
        }, '/all/client');
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
