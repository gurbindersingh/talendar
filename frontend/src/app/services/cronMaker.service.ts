import { Injectable } from '@angular/core';
import { NgbDateStruct, NgbTimeStruct } from '@ng-bootstrap/ng-bootstrap';

@Injectable({
    providedIn: 'root',
})
export class CronMakerService {
    private cronMaker: CronMakerService;
    private result: string;

    constructor(cronMaker: CronMakerService) {
        this.cronMaker = cronMaker;
    }


    //Cron Expression has the form:
    // 'startMinute/endMinute startHour/endHour startDay/endDay startMonth/endMonth
    //        startYear/endYear toggle repeatAt(O1-O4 for the 4 options) repeatX endAt endX'
    public createCron(
        vonDate: NgbDateStruct,
        vonTime: NgbTimeStruct,
        bisDate: NgbDateStruct,
        bisTime: NgbTimeStruct,
        toggle: boolean,
        repeatAt: string,
        repeatX: number,
        endAt: string,
        endX: number
    ): string {
        this.result = '';
        this.result = this.result + vonTime.minute + '/' + bisTime.minute + ' ';
        this.result = this.result + vonTime.hour + '/' + bisTime.hour + ' ';
        this.result = this.result + vonDate.day + '/' + bisDate.day + ' ';
        this.result = this.result + vonDate.month + '/' + bisDate.month + ' ';
        this.result = this.result + vonDate.year + '/' + bisDate.year + ' ';
        this.result = this.result + toggle + ' ';

        if (repeatAt === 'Nie') {
            this.result = this.result + 'O1 ';
        } else if (repeatAt === 'Jeden Tag') {
            this.result = this.result + 'O2 ';
        } else if (repeatAt === 'Jede Woche') {
            this.result = this.result + 'O3 ';
        } else {
            this.result = this.result + 'O4 ';
        }

        this.result = this.result + repeatX + ' ';
        this.result = this.result + endAt + ' ';
        this.result = this.result + endX + ' ';


        return this.result;
    }
}
