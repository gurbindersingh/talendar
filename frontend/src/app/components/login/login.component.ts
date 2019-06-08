import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { InternalUpdateService } from 'src/app/services/internal-update.service';
import { Router } from '@angular/router';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
    constructor(
        private authenticationService: AuthenticationService,
        private router: Router
    ) {}

    email: string;
    password: string;

    ngOnInit() {}

    /**
     * Login user to system (request with credentials)
     *
     * Notify event to InternalUpdateService (which will notify navigation component)
     */
    login(): void {
        this.authenticationService.login(this.email, this.password).subscribe(
            (data) => {
                console.log('Login After Request Success: ' + data);
                this.router.navigate(['/calendar']);
            },
            (error: Error) => {
                console.log('Login After Request Failure: ' + error.message);
            }
        );
    }
}
