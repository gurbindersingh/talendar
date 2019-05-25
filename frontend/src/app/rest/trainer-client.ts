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

    public getAll(): Observable<Trainer[]> {
        return super.get((error: HttpErrorResponse) => {
            console.log('HTTP GET All Trainer Failed: ' + error.message);
        }, '');
    }

    public getById(id: bigint): Observable<Trainer> {
        console.log(id);
        return super.get((error: HttpErrorResponse) => {
            console.log(
                'HTTP GET Trainer With ID ' + id + ' Failed: ' + error.message
            );
        }, '/' + id);
    }

    public update(trainer: Trainer): Observable<Trainer> {
        return super.patch(
            (error: HttpErrorResponse) => {
                console.log(
                    'HTTP PATCH To Update Trainer With ID' +
                        trainer.id +
                        ' Failed: ' +
                        error.message
                );
            },
            '',
            trainer
        );
    }
}
