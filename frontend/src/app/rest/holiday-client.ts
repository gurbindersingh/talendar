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
                console.log('HTTP POST Holiday Failed: ' + error.message);
            },
            '',
            holiday
        );
    }
}