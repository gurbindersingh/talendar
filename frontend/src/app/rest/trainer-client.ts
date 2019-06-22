import { RestClient } from './rest-client';
import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Trainer } from '../models/trainer';
import { Observable } from 'rxjs';
import { SHA3 } from 'sha3';

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

    public getById(id: number): Observable<Trainer> {
        console.log(id);
        return super.get((error: HttpErrorResponse) => {
            console.log(
                'HTTP GET Trainer With ID ' + id + ' Failed: ' + error.message
            );
        }, '/' + id);
    }

    /**
     * A new trainer is posted. And the given password will be hashed if specified and send along.
     * If no password is specified then the already existent password within trainer will be used
     * without extra hashing (i.e it is a password that has already been hashed and
     * it has not been reset)
     * @param trainer the trainer to post
     * @param password the new password to be set for this trainer
     */
    public update(trainer: Trainer, password: string): Observable<Trainer> {
        if (password !== undefined) {
            const hashed = new SHA3(512).update(password).digest('hex');
            trainer.password = hashed;
        }

        return super.put(
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

    public deleteTrainer(id: number): Observable<Trainer> {
        return super.delete((error: HttpErrorResponse) => {
            console.log('HTTP DELETE Trainer Failed: ' + error.message);
        }, '/' + id);
    }
}
