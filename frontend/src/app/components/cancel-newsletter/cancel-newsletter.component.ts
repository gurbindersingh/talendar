import { Component, OnInit } from '@angular/core';
import { CustomerClient } from '../../rest/customer-client';
import { ActivatedRoute } from '@angular/router';
import { NgForm } from '@angular/forms';

@Component({
    selector: 'app-cancel-newsletter',
    templateUrl: './cancel-newsletter.component.html',
    styleUrls: ['./cancel-newsletter.component.scss'],
})
export class CancelNewsletterComponent implements OnInit {
    private email: string;
    text: string;
    title: string;
    valid: boolean;
    successMsg: string;
    errorMsg: string;

    constructor(
        private customerClient: CustomerClient,
        private route: ActivatedRoute
    ) {}

    ngOnInit() {
        this.email = this.route.snapshot.queryParams.email;
        if (this.email === undefined) {
            this.title = 'Dieser URL ist nicht gültig';
            this.text = 'Sie hätten dieser Seite garnicht erreichen sollen';
            this.valid = false;
        } else {
            this.title = 'Wollen Sie Ihre Abonnement abrechen?';
            this.text =
                'Drücken Sie hier um Ihren E-Mail aus der Mailing-List zu entfernen.';
            this.valid = true;
        }
    }

    public cancelSubscription(form: NgForm): void {
        this.customerClient.update(this.email).subscribe(
            () => {
                this.successMsg = 'Ihr Abonnement wurde erfolgreich storniert';
            },
            (error: Error) => {
                this.errorMsg =
                    'Ihr Abonnement konnte nicht storniert werden ' +
                    error.message;
            }
        );
    }
}
