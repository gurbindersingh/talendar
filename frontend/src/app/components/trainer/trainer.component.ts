import { Component, OnInit, SimpleChanges, OnChanges } from '@angular/core';
import { NgForm } from '@angular/forms';
import { TrainerClient } from 'src/app/rest/trainer-client';
import { Trainer } from 'src/app/models/trainer';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import {
    NgbDateNativeAdapter,
    NgbDateStruct,
    NgbDate,
} from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'app-trainer',
    templateUrl: './trainer.component.html',
    styleUrls: ['./trainer.component.scss'],
    providers: [NgbDateNativeAdapter],
})
export class TrainerComponent implements OnInit {
    // these vars are accessed in the template
    title: string;
    trainer: Trainer = new Trainer();
    birthday: NgbDateStruct;
    password: string;
    passwordRepeated: string;

    btnContextDescription: string;
    errorMsg: string;
    successMsg: string;
    birthdayOptionsColumn1: any = {
        Trockeneis: { selected: false, value: 'DryIce' },
        Raketen: { selected: false, value: 'Rocket' },
        Superhelden: { selected: false, value: 'Superhero' },
    };
    birthdayOptionsColumn2: any = {
        Photo: { selected: false, value: 'Photo' },
        Malen: { selected: false, value: 'Painting' },
    };

    // only used within component
    private isSaveMode: boolean;
    private currentDate: Date = new Date();

    constructor(
        private trainerClient: TrainerClient,
        private route: ActivatedRoute,
        private location: Location,
        private adapter: NgbDateNativeAdapter
    ) {}

    getBirthdayColumn1Keys() {
        return Object.keys(this.birthdayOptionsColumn1);
    }

    getBirthdayColumn2Keys() {
        return Object.keys(this.birthdayOptionsColumn2);
    }

    getMinDateForBirth() {
        return { year: this.currentDate.getFullYear() - 80, month: 0, day: 0 };
    }

    getMaxDateForBirth() {
        return {
            year: this.currentDate.getFullYear(),
            month: this.currentDate.getMonth(),
            day: this.currentDate.getDay(),
        };
    }

    getSelectionStart() {
        if (this.isSaveMode) {
            return {
                year: this.currentDate.getFullYear() - 20,
                month: 1,
                day: 1,
            };
        } else {
            const date: Date = new Date(this.trainer.birthday);
            return {
                year: date.getFullYear(),
                // month january mapped to 0, ngbdatepcicker starts at 1
                // strange enough selecting day 1 of a month doesn't cause problems
                month: date.getUTCMonth() + 1,
                day: date.getUTCDate(),
            };
        }
    }

    /**
     * Upon loading this component, we will either save a new trainer (no id query param)
     * -> .../trainer
     * or we will edit an existing trainer (then there is an id query param)
     * -> .../trainer?id=xyz
     */
    ngOnInit() {
        // check whether this site was loaded with a query param (edit) else
        // we are in save mode
        const id: number = this.route.snapshot.queryParams.id;

        if (id === undefined) {
            this.title = 'Trainer erstellen';
            this.btnContextDescription = 'Neuen Trainer erstellen';
            this.isSaveMode = true;
        } else {
            this.title = 'Trainer Bearbeiten';
            this.btnContextDescription = 'Änderungen speichern';
            this.isSaveMode = false;
            this.trainerClient.getById(id).subscribe(
                (data: Trainer) => {
                    console.log(data);
                    // create ngb data model from trainer data and set bday in form
                    const loadedDate: Date = new Date(data.birthday);
                    console.log(loadedDate);
                    const ngbDate: NgbDate = new NgbDate(
                        loadedDate.getFullYear(),
                        loadedDate.getUTCMonth() + 1,
                        loadedDate.getUTCDate()
                    );
                    console.log(ngbDate);
                    this.birthday = ngbDate;

                    // mark active checkboxes of a trainer as selected
                    this.fillCheckboxes(data.birthdayTypes);

                    this.trainer = data;
                },
                (error: Error) => {
                    /**
                     * Even though this error situation should be very rare,
                     * this is not the smoothest solution.
                     * But what to do?!?
                     * just stay here and continue with save made (imo not sensible too)
                     */
                    this.errorMsg =
                        'Der ausgewählte Trainer konnte leider nicht geladen werden.';
                    this.location.back();
                }
            );
        }
    }

    public postTrainer(form: NgForm): void {
        const supervisedBirthdays: string[] = [];
        const allBirthdayOptions = Object.assign(
            {},
            this.birthdayOptionsColumn1,
            this.birthdayOptionsColumn2
        );

        for (const option of Object.keys(allBirthdayOptions)) {
            if (allBirthdayOptions[option].selected) {
                supervisedBirthdays.push(allBirthdayOptions[option].value);
            }
        }
        this.trainer.birthdayTypes = supervisedBirthdays;
        this.trainer.birthday = this.transformToDate(this.birthday);

        if (this.password !== this.passwordRepeated) {
            return;
        } else {
            this.trainer.password = this.password;
        }

        if (this.isSaveMode) {
            this.trainerClient.postNewTrainer(this.trainer).subscribe(
                (data: Trainer) => {
                    console.log(data);
                    this.successMsg =
                        'Der Betreuer wurde erfolgreich gespeichert';
                    form.reset();
                },
                (error: Error) => {
                    this.errorMsg =
                        'Der Betreuer konnte nicht angelegt werden: ' +
                        error.message;
                }
            );
        } else {
            this.trainerClient.update(this.trainer).subscribe(
                (data: Trainer) => {
                    console.log(data);
                    this.successMsg =
                        'Der Betreuer wurde erfolgreich aktualisiert';
                },
                (error: Error) => {
                    this.errorMsg =
                        'Der Betreuer konnte nicht erfolgreich aktualisiert werden: ' +
                        error.message;
                }
            );
        }
    }

    public isCompleted(): boolean {
        if (
            this.trainer.firstName === undefined ||
            this.trainer.firstName === ''
        ) {
            return false;
        }
        if (
            this.trainer.lastName === undefined ||
            this.trainer.lastName === ''
        ) {
            return false;
        }
        if (this.birthday === undefined) {
            return false;
        }
        if (this.trainer.email === undefined || this.trainer.email === '') {
            return false;
        }
        if (this.trainer.phone === undefined || this.trainer.phone === '') {
            return false;
        }
        if (this.password === undefined || this.password === '') {
            return false;
        }
        if (
            this.passwordRepeated === undefined ||
            this.passwordRepeated === ''
        ) {
            return false;
        }
        if (this.password !== this.passwordRepeated) {
            return false;
        }
        // todo add check for password as soon as there is a mechanism that supports passwords
        return true;
    }

    public clearInfoMsg(): void {
        this.errorMsg = undefined;
        this.successMsg = undefined;
    }

    public cancel(): void {
        this.location.back();
    }

    private fillCheckboxes(supervisedBirthdays: string[]): void {
        const birthdayOptions1 = Object.assign({}, this.birthdayOptionsColumn1);

        for (const option of Object.keys(birthdayOptions1)) {
            if (supervisedBirthdays.includes(birthdayOptions1[option].value)) {
                this.birthdayOptionsColumn1[option].selected = true;
            }
        }

        const birthdayOptions2 = Object.assign({}, this.birthdayOptionsColumn2);

        for (const option of Object.keys(birthdayOptions2)) {
            if (supervisedBirthdays.includes(birthdayOptions2[option].value)) {
                this.birthdayOptionsColumn2[option].selected = true;
            }
        }
    }

    private transformToDate(date: NgbDateStruct): Date {
        return this.adapter.toModel(date);
    }
}
