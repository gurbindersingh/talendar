import { Injectable } from '@angular/core';

@Injectable()
export class SessionStorageService {
    private isLoggedIn: any;

    constructor() {
        const retrieved = localStorage.getItem('futureToken');
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
                localStorage.removeItem('futureToken');
                localStorage.removeItem('currentToken');
            } else {
                this.isLoggedIn = true;
            }
        }
    }

    get loggedIn(): boolean {
        return this.isLoggedIn;
    }

    get sessionToken(): string {
        // we have two tokens, future one extends expiration time of current one
        // but is not valid from beginning
        // first get both
        const currentToken = localStorage.getItem('currentToken');
        const futureToken = localStorage.getItem('futureToken');

        // most likely currentToken will be null too in this case  but is is not important
        // return whatever is stores (if token cool else null is expected as deafult anyways)
        if (futureToken == null) {
            return currentToken;
        }

        const parsed = JSON.parse(atob(futureToken.split('.')[1]));

        // if current date is less than NotBefore stamp then future is not ready
        if (Date.now() / 1000 < parsed.nbf) {
            return currentToken;
        } else {
            return futureToken;
        }
    }

    setLoggedIn(loggedIn: any): void {
        console.log('SessionStorage Service: ');
        console.log('CurrentToken: ' + loggedIn.currentToken);
        console.log('FutureToken: ' + loggedIn.futureToken);
        if (loggedIn === false) {
            this.isLoggedIn = false;
            localStorage.removeItem('currentToken');
            localStorage.removeItem('futureToken');
        } else {
            this.isLoggedIn = true;
            localStorage.setItem('currentToken', loggedIn.currentToken);
            localStorage.setItem('futureToken', loggedIn.futureToken);
        }
    }
}
