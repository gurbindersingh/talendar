import { Injectable } from '@angular/core';
import { RestClient } from './rest-client';
import { ConsultationTime } from '../models/consulationTime';
import { Observable } from 'rxjs';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';

@Injectable()
export class ConsultationTimeClient extends RestClient {
    constructor(httpClient: HttpClient) {
        super('consultingTime', httpClient);
    }
    public postNewConsultationTime(
        consultationTime: ConsultationTime
    ): Observable<ConsultationTime> {
        return super.post(
            (error: HttpErrorResponse) => {
                console.log(
                    'HTTP POST ConsultationTime Failed: ' + error.message
                );
            },
            '',
            consultationTime
        );
    }

    public getAllConsultationTimes_trainerView(
        id: number
    ): Observable<ConsultationTime[]> {
        return super.get((error: HttpErrorResponse) => {
            console.log(
                'HTTP GET All ConsultationTime (For trainer) Failed: ' +
                    error.message
            );
        }, '/' + id);
    }

    public getAllConsultationTimes_adminView(): Observable<ConsultationTime[]> {
        return super.get((error: HttpErrorResponse) => {
            console.log(
                'HTTP GET All ConsultationTime (For admin) Failed: ' +
                    error.message
            );
        });
    }
}
