import { Injectable } from '@angular/core';
import { RestClient } from './rest-client';
import { Holiday } from '../models/holiday';
import { Observable } from 'rxjs';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';

@Injectable()
export class HolidayClient extends RestClient {
    constructor(httpClient: HttpClient) {
        super('holiday', httpClient);
    }
    public postNewHoliday(holiday: Holiday): Observable<Holiday> {
        return super.post(
            (error: HttpErrorResponse) => {
                
            },
            '',
            holiday
        );
    }

    public getAllHolidays_trainerView(id: number): Observable<Holiday[]> {
        return super.get((error: HttpErrorResponse) => {
            console.log(
                'HTTP GET All Holidays (For trainer) Failed: ' + error.message
            );
        }, '/' + id);
    }

    public getAllHolidays_adminView(id: number): Observable<Holiday[]> {
        return super.get((error: HttpErrorResponse) => {
            console.log(
                'HTTP GET All Holidays (For trainer) Failed: ' + error.message
            );
        }, '/' + id);
    }
}
