import { HttpClient, HttpErrorResponse } from '@angular/common/http';

import { environment } from 'src/environments/environment';
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
        params?: any,
        headers?: any,
        responseType?: any
    ): Observable<T> {
        return this.request(
            'get',
            onError,
            url,
            params,
            null,
            headers,
            responseType
        );
    }

    protected post<T>(
        onError: (error: HttpErrorResponse) => void,
        url: string,
        body: any,
        params?: any,
        headers?: any,
        responseType?: any
    ): Observable<T> {
        return this.request(
            'post',
            onError,
            url,
            params,
            body,
            headers,
            responseType
        );
    }

    protected put<T>(
        onError: (error: HttpErrorResponse) => void,
        url: string,
        body: any,
        params?: any,
        headers?: any,
        responseType?: any
    ): Observable<T> {
        return this.request(
            'put',
            onError,
            url,
            params,
            body,
            headers,
            responseType
        );
    }

    protected patch<T>(
        onError: (error: HttpErrorResponse) => void,
        url: string,
        body: any,
        params?: any,
        headers?: any,
        responseType?: any
    ): Observable<T> {
        return this.request(
            'patch',
            onError,
            url,
            params,
            body,
            headers,
            responseType
        );
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
        body?: any,
        headers?: any,
        responseType?: any
    ): Observable<T> {
        const fullUrl = url ? this.baseUrl + url : this.baseUrl;
        return this.httpClient
            .request<T>(method, fullUrl, {
                body,
                params,
                headers,
                responseType,
            })
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
    }
}
