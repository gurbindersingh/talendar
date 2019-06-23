import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { NotificationService } from 'src/app/services/notification.service';
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

    errorMsg: string;

    ngOnInit() {}

    /**
     * Login user to system (request with credentials)
     *
     * Notify event to InternalUpdateService (which will notify navigation component)
     */
    login(): void {
        this.authenticationService.login(this.email, this.password).subscribe(
            (data) => {
                
                this.router.navigate(['/calendar']);
            },
            (error: Error) => {
                
                this.errorMsg =
                    'Die verwendeten Anmeldedaten konnten keinem Konto zugeordnet werden';
            }
        );
    }
}
