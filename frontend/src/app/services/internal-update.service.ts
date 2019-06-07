import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import { shareReplay } from 'rxjs/operators';

@Injectable({
    providedIn: 'root',
})
export class InternalUpdateService {
    public loginStatusChanges: Subject<boolean>;
    public loginStatusChanges$: Observable<boolean>;

    constructor() {
        this.loginStatusChanges = new Subject<boolean>();
        this.loginStatusChanges$ = this.loginStatusChanges.asObservable();
    }

    notifyLogin(): void {
        this.loginStatusChanges.next(true);
    }

    notifyLogout(): void {
        this.loginStatusChanges.next(true);
    }
}
