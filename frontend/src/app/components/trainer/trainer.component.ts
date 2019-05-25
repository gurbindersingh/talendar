import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { TrainerClient } from 'src/app/rest/trainer-client';
import { Trainer } from 'src/app/models/trainer';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';

@Component({
    selector: 'app-trainer',
    templateUrl: './trainer.component.html',
    styleUrls: ['./trainer.component.scss'],
})
export class TrainerComponent implements OnInit {
    private title: string;
    private trainer: Trainer = new Trainer();
    private isSaveMode: boolean;
    private btnContextDescription: string;
    private errorMsg: string;
    private successMsg: string;

    constructor(
        private trainerClient: TrainerClient,
        private route: ActivatedRoute,
        private router: Router,
        private location: Location
    ) {}

    /**
     * Upon loading this component, we will either save a new trainer (no id query param)
     * -> .../trainer
     * or we will edit an existing trainer (then there is an id query param)
     * -> .../trainer?id=xyz
     */
    ngOnInit() {
        console.log('Initialize trainer component');

        const id: bigint = this.route.snapshot.queryParams.id;

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
                },
                (error: Error) => {
                    /**
                     * Even though this error situation should be very rare,
                     * this is not the smoothest solution.
                     * But what to do?!?
                     * just stay here and continue with save made (imo not sensible too)
                     */
                    alert(
                        'The requested trainer could not be laoded: ' +
                            error.message
                    );
                    this.location.back();
                }
            );
        }
    }

    public postTrainer(form: NgForm): void {
        console.log('Pass Form Data To Rest Client');

        if (this.isSaveMode) {
            this.trainerClient.postNewTrainer(this.trainer).subscribe(
                (data: Trainer) => {
                    console.log(data);
                    this.successMsg =
                        'Der Betreuer wurde erfolgreich gespeichert';
                },
                (error: Error) => {
                    console.log(error.message);
                    this.errorMsg =
                        'Der Betreuer konnte nicht angelegt werden!';
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
                    console.log(error.message);
                    this.errorMsg =
                        'Der Betreuer konnte nicht erfolgreich aktualisiert werden';
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
        if (this.trainer.birthday === undefined) {
            return false;
        }
        if (this.trainer.email === undefined || this.trainer.email === '') {
            return false;
        }
        if (this.trainer.phone === undefined || this.trainer.phone === '') {
            return false;
        }
        return true;
    }

    public clearInfoMsg(): void {
        this.errorMsg = undefined;
        this.successMsg = undefined;
    }

    public cancel(): void {
        this.location.back();
    }
}
