import { RestClient } from './rest-client';
import { Observable } from 'rxjs';
import {
    HttpErrorResponse,
    HttpClient,
    HttpHeaders,
} from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable()
export class ImageClient extends RestClient {
    constructor(httpClient: HttpClient) {
        super('upload', httpClient);
    }

    public postProfilePicture(data: FormData): Observable<string> {
        console.log(data);

        const payload: FormDataEntryValue = data.get('file');

        console.log(payload);

        return this.post(
            (error: HttpErrorResponse) => {
                console.log(
                    'HTTP POST Profile Picture Failed: ' + error.message
                );
            },
            '/image/trainer',
            data
        );
    }
}
