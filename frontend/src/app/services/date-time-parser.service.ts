import { Injectable } from '@angular/core';
import {
    NgbDateParserFormatter,
    NgbDateStruct,
    NgbTimeStruct,
} from '@ng-bootstrap/ng-bootstrap';

@Injectable({
    providedIn: 'root',
})
export class DateTimeParserService {
    private parserFormatter: NgbDateParserFormatter;

    constructor(parserFormatter: NgbDateParserFormatter) {
        this.parserFormatter = parserFormatter;
    }

    public dateToString(date: NgbDateStruct): string {
        return this.parserFormatter.format(date);
    }

    public dateTimeToString(date: NgbDateStruct, time: NgbTimeStruct): string {
        let stringMinute = '' + time.minute;
        let stringHour = '' + time.hour;
        let isChangedHour = false;
        let isChangedMinute = false;

        if (time.minute === 0) {
            stringMinute = '00';
            isChangedMinute = true;
        }
        if (time.hour < 10) {
            stringHour = '0' + time.hour;
            isChangedHour = true;
        }

        return (
            this.parserFormatter.format(date) +
            'T' +
            stringHour +
            ':' +
            stringMinute +
            ':00'
        );
    }

    // required string format from backend
    public stringToDate(date: string): NgbDateStruct {
        const splitted = date.split('T')[0].split('-');
        return { year: +splitted[0], month: +splitted[1], day: +splitted[2] };
    }

    // required string format from backend
    public stringToTime(date: string): NgbTimeStruct {
        const splitted = date.split('T')[1].split(':');
        return { hour: +splitted[0], minute: +splitted[1], second: 0 };
    }
}
