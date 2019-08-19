import { Injectable } from "@angular/core";
import { RestClient } from './rest-client';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Birthday } from './../models/birthday';


@Injectable()
export class BirthdayClient extends RestClient {
    constructor(httpClient: HttpClient) {
        super('birthdayTypes', httpClient);
    }

    public postBirthdayType(birthday: Birthday): Observable<Birthday> {
        return super.post(
            (error: HttpErrorResponse) => {
                console.log('HTTP POST Birthday-Type Failed: ' + error.message);
            },
            '',
            birthday
        );
    }

    public getAllBirthdayTypes(): Observable<Birthday[]> {
        return super.get((error: HttpErrorResponse) => {
            console.log('HTTP GET All Birthday-Types Failed: ' + error.message);
        }, '');
    }

    public deleteBirthdayType(birthday: Birthday): Observable<Birthday> {
        console.log(birthday);
        return super.delete(
            (error: HttpErrorResponse) => {
                console.log('HTTP DELETE Tag Failed: ' + error.message);
            },
            '/' + birthday.id
        );
    }

}