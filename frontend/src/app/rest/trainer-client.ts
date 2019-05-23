import { RestClient } from './rest-client';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Trainer } from '../models/trainer';
import { Observable } from 'rxjs';

@Injectable()
export class TrainerClient extends RestClient {
    constructor(httpClient: HttpClient) {
        super('trainers', httpClient);
    }

    public postNewTrainer(trainer: Trainer): Observable<Trainer> {
        return super.post(
            (error: HttpErrorResponse) => {
                console.log('HTTP POST Trainer Failed: ' + error.message);
            },
            '',
            trainer
        );
    }
}
