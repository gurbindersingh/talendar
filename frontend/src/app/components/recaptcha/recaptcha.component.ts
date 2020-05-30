import { Component, OnInit, OnDestroy, AfterViewInit } from '@angular/core';

@Component({
    selector: 'app-recaptcha',
    templateUrl: './recaptcha.component.html',
    styleUrls: ['./recaptcha.component.scss'],
})
export class RecaptchaComponent implements OnInit, OnDestroy {
    script: HTMLScriptElement;

    constructor() {}

    ngOnInit(): void {
        try {
            window.grecaptcha.render('g-recaptcha', {
                sitekey: '6Lc2QrAUAAAAAG4JI4emazC6AAXfcMKuDC25n2ze',
            });
        } catch (error) {
            
        }
    }

    ngOnDestroy(): void {}
}
