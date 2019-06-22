import { RestClient } from './rest-client';
import { Observable } from 'rxjs';
import {
    HttpErrorResponse,
    HttpClient,
    HttpHeaders,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Type } from '@angular/compiler/src/core';

@Injectable()
export class ImageClient extends RestClient {
    constructor(httpClient: HttpClient) {
        super('upload', httpClient);
    }

    public postProfilePicture(data: FormData): Observable<string> {
        return super.post(
            (error: HttpErrorResponse) => {
                console.log(
                    'HTTP POST Profile Picture Failed: ' + error.message
                );
            },
            '/image/trainer',
            data,
            null,
            null,
            'text'
        );
    }

    public postCoursePicture(data: FormData): Observable<string> {
        return super.post(
            (error: HttpErrorResponse) => {
                console.log(
                    'HTTP POST Course Pictures Failed: ' + error.message
                );
            },
            '/image/course',
            data,
            null,
            null,
            'text'
        );
    }

    public getProfilePicture(fileName: string): Observable<any> {
        return super.get(
            (error: HttpErrorResponse) => {
                console.log(
                    'HTTP GET Profile Picture Failed: ' + error.message
                );
            },
            '/image/trainer',
            { name: fileName },
            null,
            'arraybuffer'
        );
    }
}
