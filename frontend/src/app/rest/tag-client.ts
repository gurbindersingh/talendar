import { Injectable } from "@angular/core";
import { RestClient } from './rest-client';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Tag } from './../models/tag'


@Injectable()
export class TagClient extends RestClient {
    constructor(httpClient: HttpClient) {
        super('tags', httpClient);
    }

    public postTag(tag: Tag): Observable<Tag> {
        return super.post(
            (error: HttpErrorResponse) => {
                console.log('HTTP POST Tag Failed: ' + error.message);
            },
            '',
            tag
        );
    }

    public getAll(): Observable<Tag[]> {
        return super.get((error: HttpErrorResponse) => {
            console.log('HTTP GET All Tags Failed: ' + error.message);
        }, '');
    }

    public deleteTag(tag: Tag): Observable<Tag> {
        return super.delete((error: HttpErrorResponse) => {
            console.log('HTTP DELETE Tag Failed: ' + error.message);
        }, '/' + tag.id);
    }

}