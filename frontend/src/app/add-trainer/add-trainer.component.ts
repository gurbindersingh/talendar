import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';

@Component({
    selector: 'app-add-trainer',
    templateUrl: './add-trainer.component.html',
    styleUrls: ['./add-trainer.component.scss'],
})
export class AddTrainerComponent implements OnInit {
    constructor() {}

    ngOnInit() {}

    public postTrainer(form: NgForm) {
        console.log(form);
    }
}
