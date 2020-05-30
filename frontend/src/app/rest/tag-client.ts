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
                
            },
            '',
            tag
        );
    }

    public getAll(): Observable<Tag[]> {
        return super.get((error: HttpErrorResponse) => {
            
        }, '');
    }

    public deleteTag(tag: Tag): Observable<Tag> {
        return super.delete((error: HttpErrorResponse) => {
            
        }, '/' + tag.id);
    }

}