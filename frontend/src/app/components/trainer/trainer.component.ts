import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { TrainerClient } from 'src/app/rest/trainer-client';
import { Trainer } from 'src/app/models/trainer';
import { HttpResponse } from '@angular/common/http';

@Component({
    selector: 'app-trainer',
    templateUrl: './trainer.component.html',
    styleUrls: ['./trainer.component.scss'],
})
export class TrainerComponent implements OnInit {
    private title = 'Betreuer erstellen';
    public trainer: Trainer = new Trainer();

    constructor(private trainerClient: TrainerClient) {}

    ngOnInit() {}

    public postTrainer(form: NgForm): void {
        console.log('Pass Form Data To Rest Client');
        this.trainerClient.postNewTrainer(this.trainer).subscribe(
            (data: Trainer) => {
                console.log(data);
            },
            (error) => {
                console.log(error);
            }
        );
    }

    public isCompleted(): boolean {
        if (this.trainer.firstName === '') {
            return false;
        }
        if (this.trainer.lastName === '') {
            return false;
        }

        // date check
        if (this.trainer.email === '') {
            return false;
        }
        if (this.trainer.phone === '') {
            return false;
        }
        return true;
    }
}
