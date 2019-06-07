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
