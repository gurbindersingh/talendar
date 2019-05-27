import {
    HttpClient,
    HttpErrorResponse,
    HttpParams,
} from '@angular/common/http';

import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { share } from 'rxjs/operators';
import { catchError } from 'rxjs/operators';

export abstract class RestClient {
    // The baseUrl points to the Backend.
    private readonly baseUrl: string;

    protected constructor(
        endpointUrl: string,
        private readonly httpClient: HttpClient
    ) {
        this.baseUrl = environment.apiEndpoint + endpointUrl;
    }

    /**
     * @param onError A callback to be executed in case of an error.
     */
    protected get<T>(
        onError: (error: HttpErrorResponse) => void,
        url?: string,
        params?: any
    ): Observable<T> {
        return this.request('get', onError, url, params);
    }

    protected post<T>(
        onError: (error: HttpErrorResponse) => void,
        url: string,
        body: any,
        params?: any
    ): Observable<T> {
        return this.request('post', onError, url, params, body);
    }

    protected put<T>(
        onError: (error: HttpErrorResponse) => void,
        url: string,
        body: any,
        params?: any
    ): Observable<T> {
        return this.request('put', onError, url, params, body);
    }

    protected patch<T>(
        onError: (error: HttpErrorResponse) => void,
        url: string,
        body: any,
        params?: any
    ): Observable<T> {
        return this.request('patch', onError, url, params, body);
    }

    protected delete<T>(
        onError: (error: HttpErrorResponse) => void,
        url: string,
        params?: any
    ): Observable<T> {
        return this.request('delete', onError, url, params);
    }

    // param is either of type HttpParams or object type as key - value pairs
    // maybe consider making the type more explicit?
    private request<T>(
        method: string,
        onError: (error: HttpErrorResponse) => void,
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
                    onError(response);
                    // not sure if throwing here is necessary
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
