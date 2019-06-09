import { Injectable } from '@angular/core';
import { NgbDateStruct, NgbTimeStruct } from '@ng-bootstrap/ng-bootstrap';

@Injectable({
    providedIn: 'root',
})
export class ClickedDateService {
    private clickedDate: Date;

    constructor() {}

    setDateTime(date: Date) {
        this.clickedDate = date;
    }

    public getDate(): NgbDateStruct {
        return { day: 1, month: 1, year: 1970 };
    }

    public getTime(): NgbTimeStruct {
        return { hour: 0, minute: 0, second: 0 };
    }
}
