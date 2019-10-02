import { Injectable } from '@angular/core';
import { RestClient } from './rest-client';
import { ConsultationTime } from '../models/consulationTime';
import { Observable } from 'rxjs';
import { HttpErrorResponse, HttpClient } from '@angular/common/http';
import { ConsultationTimes } from '../models/consultationTimes';

@Injectable()
export class ConsultationTimesClient extends RestClient {
    constructor(httpClient: HttpClient) {
        super('consultingTimes', httpClient);
    }

    public postNewConsultationTimes(
        consultationTimes: ConsultationTimes
    ): Observable<ConsultationTime[]> {
        return super.post(
            (error: HttpErrorResponse) => {
                console.log(
                    'HTTP POST ConsultationTimes Failed: ' + error.message
                );
            },
            '',
            consultationTimes
        );
    }
}
