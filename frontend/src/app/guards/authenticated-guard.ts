import {
    CanActivate,
    RouterStateSnapshot,
    ActivatedRouteSnapshot,
    Router,
} from '@angular/router';
import { Injectable } from '@angular/core';
import { AuthenticationService } from '../services/authentication.service';

@Injectable()
export class AuthenticatedGuard implements CanActivate {
    constructor(private authenticationService: AuthenticationService) {}

    canActivate(
        route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot
    ): boolean {
        return this.authenticationService.isLoggedIn;
    }
}
