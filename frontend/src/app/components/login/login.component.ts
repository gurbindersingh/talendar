import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
    constructor(private authenticationService: AuthenticationService) {}

    email: string;
    password: string;

    ngOnInit() {}

    login(): void {
        this.authenticationService.login(this.email, this.password).subscribe(
            (data) => {
                console.log('Login After Request Success: ' + data);
            },
            (error: Error) => {
                console.log('Login After Request Failure: ' + error.message);
            }
        );
    }
}
