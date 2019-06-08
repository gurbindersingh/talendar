import { Injectable } from '@angular/core';
import { Subject, Observable } from 'rxjs';
import { shareReplay } from 'rxjs/operators';

@Injectable({
    providedIn: 'root',
})
export class InternalUpdateService {
    /**
     * Subjects can eiter emit and receive events.
     * We will only use it to emit events that notify about login/logout updates
     */
    private loginStatusChanges: Subject<boolean>;
    /**
     * Observable will be linked to prior subject.
     * Anyone can subscribe to it, in order to be notified about the subjects
     * events.
     */
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
