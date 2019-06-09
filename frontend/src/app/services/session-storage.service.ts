import { Injectable } from '@angular/core';

@Injectable()
export class SessionStorageService {
    private isLoggedIn: any;

    constructor() {
        const retrieved = localStorage.getItem('loggedIn');
        console.log(
            '===============================\n' +
                'Retrieved from local storage: \n' +
                retrieved +
                '\n===============================\n'
        );

        if (retrieved == null || retrieved === undefined) {
            this.isLoggedIn = false;
        } else {
            const token = JSON.parse(atob(retrieved.split('.')[1]));
            console.log(
                '===============================\n' +
                    'ONE TOKEN TO RULE THEM ALL: \n' +
                    token +
                    '\n===============================\n'
            );

            // Date.now() returns miliseconds since 01.01.1970 (UNIX time)
            // JWT expiration is definded in seconds (also UNIX time)
            if (token == null || token.exp < Date.now() / 1000) {
                this.isLoggedIn = false;
                localStorage.removeItem('loggedIn');
            } else {
                this.isLoggedIn = true;
            }
        }
    }

    get loggedIn(): boolean {
        return this.isLoggedIn;
    }

    get sessionToken(): string {
        return localStorage.getItem('loggedIn');
    }

    setLoggedIn(loggedIn: any): void {
        if (loggedIn === false) {
            this.isLoggedIn = false;
            localStorage.removeItem('loggedIn');
        } else {
            this.isLoggedIn = true;
            localStorage.setItem('loggedIn', loggedIn.jwt);
        }
    }
}
