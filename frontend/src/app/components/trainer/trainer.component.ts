import { Component, OnInit, SimpleChanges, OnChanges } from '@angular/core';
import { NgForm } from '@angular/forms';
import { TrainerClient } from 'src/app/rest/trainer-client';
import { Trainer } from 'src/app/models/trainer';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import {
    NgbDateNativeAdapter,
    NgbDateStruct,
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
    btnContextDescription: string;
    errorMsg: string;
    successMsg: string;
    birthdayOptionsColumn1: any = {
        Trockeneis: false,
        Raketen: false,
        Superhelden: false,
    };
    birthdayOptionsColumn2: any = {
        Photo: false,
        Malen: false,
    };

    // only used within component
    private isSaveMode: boolean;

    constructor(
        private trainerClient: TrainerClient,
        private route: ActivatedRoute,
        private router: Router,
        private location: Location,
        private adapter: NgbDateNativeAdapter
    ) {}

    getBirthdayColumn1Keys() {
        return Object.keys(this.birthdayOptionsColumn1);
    }

    getBirthdayColumn2Keys() {
        return Object.keys(this.birthdayOptionsColumn2);
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
            this.title = 'Trainer Erstellen';
            this.btnContextDescription = 'Neuen Trainer erstellen';
            this.isSaveMode = true;
        } else {
            this.title = 'Trainer Bearbeiten';
            this.btnContextDescription = 'Änderungen speichern';
            this.isSaveMode = false;
            this.trainerClient.getById(id).subscribe(
                (data: Trainer) => {
                    console.log(data);
                    this.trainer = data;
                    this.birthday = this.transformToNgbDate(data.birthday);
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
            if (allBirthdayOptions[option]) {
                supervisedBirthdays.push(option + ' Geburtstag');
            }
        }
        this.trainer.birthdayTypes = supervisedBirthdays;
        this.trainer.birthday = this.transformToDate(this.birthday);

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

    private transformToDate(date: NgbDateStruct): Date {
        return this.adapter.toModel(date);
    }

    private transformToNgbDate(date: Date): NgbDateStruct {
        return this.adapter.fromModel(date);
    }
}
