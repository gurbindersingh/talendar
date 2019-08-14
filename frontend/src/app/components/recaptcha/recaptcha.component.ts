import { Component, OnInit, OnDestroy } from '@angular/core';

@Component({
    selector: 'app-recaptcha',
    templateUrl: './recaptcha.component.html',
    styleUrls: ['./recaptcha.component.scss'],
})
export class RecaptchaComponent implements OnInit, OnDestroy {
    form: HTMLFormElement;
    script: HTMLScriptElement;

    constructor() {}

    ngOnInit() {}

    ngOnDestroy(): void {}
}
