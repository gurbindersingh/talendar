import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';

@Component({
    selector: 'app-trainer',
    templateUrl: './trainer.component.html',
    styleUrls: ['./trainer.component.scss'],
})
export class TrainerComponent implements OnInit {
    private title = 'Betreuer erstellen';

    constructor() {}

    ngOnInit() {}

    public postTrainer(form: NgForm): void {
        console.log(form);
    }
}
