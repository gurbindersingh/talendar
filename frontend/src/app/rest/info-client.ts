import { RestClient } from './rest-client';
import { Injectable } from '@angular/core';
import {
    HttpClient,
    HttpErrorResponse,
    HttpResponse,
} from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable()
export class InfoClient {
    url: string;
    constructor(private httpClient: HttpClient) {}

    public getPdf(mail: string) {
        this.url = environment.apiEndpoint + 'info/' + mail;
        const httpOptions = {
            responseType: 'arraybuffer' as 'json',
        };
        return this.httpClient.get<any>(this.url, httpOptions);
    }
}
