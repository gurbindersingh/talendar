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

    /**
     * Returns a NgbDateStruct with either the Date of
     * the clicked slot in the calendar, or todays date.
     */
    public getDate() {
        const now = new Date(Date.now());

        let date: NgbDateStruct = {
            day: now.getDate(),
            month: now.getMonth() + 1,
            year: now.getFullYear(),
        };

        if (this.clickedDate !== undefined) {
            date = {
                day: this.clickedDate.getDate(),
                month: this.clickedDate.getMonth() + 1,
                year: this.clickedDate.getFullYear(),
            };
        }
        return date;
    }

    /**
     * Returns a NgbTimeStruct with either the Time of
     * the clicked slot in the calendar, or 13:00.
     */
    public getTime() {
        let time: NgbTimeStruct = { hour: 13, minute: 0, second: 0 };

        if (
            this.clickedDate !== undefined &&
            this.clickedDate.getHours() !== 0
        ) {
            time = {
                hour: this.clickedDate.getHours(),
                minute: this.clickedDate.getMinutes(),
                second: this.clickedDate.getSeconds(),
            };
        }
        return time;
    }
}
