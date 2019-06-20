import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { TrainerClient } from 'src/app/rest/trainer-client';
import { Trainer } from 'src/app/models/trainer';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';
import {
    NgbDateNativeAdapter,
    NgbDateStruct,
    NgbDate,
    NgbModal,
} from '@ng-bootstrap/ng-bootstrap';

import { ImageClient } from 'src/app/rest/image-client';
import * as Croppie from 'croppie';

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
    binaryEncodedImage: any = null;

    btnContextDescription: string;
    pwPlaceholder: string;
    pwRepeatPlaceholder: string;
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

    private formData: FormData = null;
    private image: File;

    private croppie: Croppie;

    constructor(
        private trainerClient: TrainerClient,
        private imageClient: ImageClient,
        private route: ActivatedRoute,
        private location: Location,
        private adapter: NgbDateNativeAdapter,
        private modalService: NgbModal
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
        this.pwPlaceholder = 'Neues Passwort';
        this.pwRepeatPlaceholder = 'Passwort wiederholen';

        if (id === undefined) {
            this.title = 'Trainer erstellen';
            this.btnContextDescription = 'Neuen Trainer erstellen';
            this.isSaveMode = true;
        } else {
            this.title = 'Trainer Bearbeiten';
            this.btnContextDescription = 'Änderungen speichern';
            this.pwPlaceholder += ' (Optional)';
            this.isSaveMode = false;
            this.trainerClient.getById(id).subscribe(
                (data: Trainer) => {
                    // create ngb data model from trainer data and set bday in form
                    const loadedDate: Date = new Date(data.birthday);
                    const ngbDate: NgbDate = new NgbDate(
                        loadedDate.getFullYear(),
                        loadedDate.getUTCMonth() + 1,
                        loadedDate.getUTCDate()
                    );
                    this.birthday = ngbDate;

                    // mark active checkboxes of a trainer as selected
                    this.fillCheckboxes(data.birthdayTypes);

                    this.trainer = data;

                    // load profile pic if picture (name of it) is associated with trainer
                    if (this.trainer.picture != null) {
                        this.imageClient
                            .getProfilePicture(this.trainer.picture)
                            .subscribe(
                                // received data are octet stream (pure 'raw' binary data)
                                (bytes: any) => {
                                    const blob = new Blob([bytes]);
                                    this.extractBinaryDataFromFile(blob);
                                },
                                (error) => {
                                    // manual parsing required because (returntype is not json)
                                    const info = JSON.parse(error);
                                    console.log(
                                        'profile picture could not be loaded: ' +
                                            info.message
                                    );
                                }
                            );
                    }
                },
                (error: Error) => {
                    this.errorMsg =
                        'Der ausgewählte Trainer konnte leider nicht geladen werden.';
                    this.location.back();
                }
            );
        }
    }

    public submitForm(form: NgForm): void {
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
        }

        if (this.password !== undefined && this.password !== '') {
            this.trainer.password = this.password;
        }

        if (this.formData == null) {
            // immediately pefrom request (PUT or POST dependant on state)
            this.postData(form);
        } else {
            // first post the selected image, if successful post trainer
            this.imageClient.postProfilePicture(this.formData).subscribe(
                (fileLocation: string) => {
                    this.trainer.picture = fileLocation;
                    this.postData(form);
                },
                (error) => {
                    // manual parsing required because this endpoint returns plain text (no json)
                    const info = JSON.parse(error);
                    this.errorMsg = info.message;
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
        // whenever password fields are not equals disable submit
        if (this.password !== this.passwordRepeated) {
            return false;
        }
        // in save mode pw has to be specified, in edit mode it may be unspecified (== no changes)
        if (this.isSaveMode) {
            if (this.password === undefined || this.password === '') {
                return false;
            }
            if (
                this.passwordRepeated === undefined ||
                this.passwordRepeated === ''
            ) {
                return false;
            }
        }

        // todo add check for password as soon as there is a mechanism that supports passwords
        return true;
    }

    public onFileSelected(event: any, croppieModal: any): void {
        const selected: File = event.target.files[0];

        // when the file selection menu is closed without selection of file
        if (selected == null) {
            return;
        }
        this.image = selected;

        this.extractBinaryDataFromFile(this.image);

        this.modalService.open(croppieModal);

        setTimeout(() => {
            const img = document.getElementById('profilePicture');
            // console.warn(img);
            this.croppie = new Croppie(img as HTMLImageElement, {
                viewport: { width: 200, height: 200 },
                boundary: { width: 250, height: 250 },
                showZoomer: true,
            });
        }, 100);
    }

    public saveCropped() {
        // this.croppie
        this.croppie
            .result({ type: 'blob', quality: 1, format: 'jpeg' })
            .then((image: Blob) => {
                console.warn('saving image');

                this.image = image as File;

                this.formData = new FormData();
                this.formData.append('file', this.image);
            })
            .catch((error) => {
                console.log(error);
                this.errorMsg =
                    'Das Bild konnte leider nicht gespeichert werden. ' +
                    'Bitte versuchen Sie es erneut.';
            });
    }

    public clearInfoMsg(): void {
        this.errorMsg = undefined;
        this.successMsg = undefined;
    }

    public cancel(): void {
        this.location.back();
    }

    /**
     * Post the form data to server.
     * In edit mode an update (PUT request) will be performed.
     * In save mode a POST request will be performed.
     */
    private postData(form: NgForm): void {
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
            this.trainerClient.update(this.trainer, this.password).subscribe(
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

    /**
     * This method can be used to extract the content of a file as binary data.
     * I.e. <img src"..."> can display images given their binary representation.
     *
     * @param file the wrapper of the content. 'File' can be also used as param as
     *             it extends Blob!
     */
    private extractBinaryDataFromFile(file: Blob): void {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = () => {
            this.binaryEncodedImage = reader.result;
        };
    }

    private transformToDate(date: NgbDateStruct): Date {
        return this.adapter.toModel(date);
    }
}
