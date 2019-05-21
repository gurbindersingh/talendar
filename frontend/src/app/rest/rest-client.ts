import {
    HttpClient,
    HttpErrorResponse,
    HttpParams,
} from '@angular/common/http';

import 'rxjs/add/observable/throw';
import 'rxjs/add/operator/catch';
import 'rxjs/add/operator/share';

import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { share } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';

export abstract class RestClient {
    private readonly baseUrl: string;

    protected constructor(
        endpointUrl: string,
        private readonly httpClient: HttpClient
    ) {
        this.baseUrl = environment.apiEndpoint + endpointUrl;
    }

    protected get<T>(url?: string, params?: any): Observable<T> {
        return this.request('get', url, params);
    }

    protected post<T>(url: string, body: any, params?: any): Observable<T> {
        return this.request('post', url, params, body);
    }

    protected put<T>(url: string, body: any, params?: any): Observable<T> {
        return this.request('put', url, params, body);
    }

    protected patch<T>(url: string, body: any, params?: any): Observable<T> {
        return this.request('patch', url, params, body);
    }

    protected delete<T>(url: string, params?: any): Observable<T> {
        return this.request('delete', url, params);
    }

    // param is either of type HttpParams or object type as key - value pairs
    // maybe consider making the type more explicit?

    private request<T>(
        method: string,
        url?: string,
        params?: any,
        body?: any
    ): Observable<T> {
        const fullUrl = url ? this.baseUrl + url : this.baseUrl;
        return this.httpClient
            .request<T>(method, fullUrl, { body, params })
            .pipe(
                share(),
                catchError((response: HttpErrorResponse) => {
                    console.log(`Request to ${fullUrl} failed:`, response);
                    throw response.status && response.error
                        ? response.error
                        : response;
                })
            );

        /* .catch((response: HttpErrorResponse) => {
                console.log(`Request to ${fullUrl} failed:`, response);
                return Observable.throw(
                    response.status && response.error
                        ? response.error
                        : response
                );
            }); */
    }
}
