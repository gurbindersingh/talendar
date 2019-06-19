import {
    CanActivate,
    RouterStateSnapshot,
    ActivatedRouteSnapshot,
} from '@angular/router';
import { Injectable } from '@angular/core';
import { AuthenticationService } from '../services/authentication.service';
import { UserDetails } from '../models/user-details';
import { Observable } from 'rxjs';
import { Authorities } from '../models/enum/authorities';

@Injectable()
export class AdminGuard implements CanActivate {
    constructor(private authenticationService: AuthenticationService) {}

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): Promise<boolean> {
        /**
         * Transformed to promise simply because obvioulsy it is not allowed
         * to handle a subscription within an Guars (caused error).
         *
         * Returning an Observable<boolean> would have been possible too, but as
         * I had to map anyway (because Observable<UserDetails> does not fit) I
         * have chosen to go with promises which I am more fmailiar with
         */
        return new Promise<boolean>((resolve) => {
            const details: Observable<
                UserDetails
            > = this.authenticationService.getUserDetails();

            // details could not be loaded (no current login)
            if (details == null) {
                resolve(false);
            }

            details.toPromise().then((data: UserDetails) => {
                if (data.roles.includes(Authorities.ADMIN)) {
                    resolve(true);
                } else {
                    resolve(false);
                }
            });
        });
    }
}
