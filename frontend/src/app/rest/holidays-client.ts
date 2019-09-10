import { Injectable } from '@angular/core';
import { RestClient } from './rest-client';
import { Holiday } from '../models/holiday';
import { Observable } from 'rxjs';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';
import { Holidays } from '../models/holidays';

@Injectable()
export class HolidaysClient extends RestClient {
    constructor(httpClient: HttpClient) {
        super('holidays', httpClient);
    }

    public postNewHolidays(holidays: Holidays): Observable<Holiday[]> {
        return super.post(
            (error: HttpErrorResponse) => {
                
            },
            '',
            holidays
        );
    }
}
