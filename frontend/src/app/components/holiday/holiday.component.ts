import { Component, OnInit } from '@angular/core';
import { Holiday } from 'src/app/models/holiday';
import {
    NgbDateParserFormatter,
    NgbDateStruct,
    NgbTimeStruct,
} from '@ng-bootstrap/ng-bootstrap';
import { NgForm } from '@angular/forms';
import { Trainer } from 'src/app/models/trainer';
import { HolidayClient } from 'src/app/rest/holiday-client';
import { DateTimeParserService } from 'src/app/services/date-time-parser.service';

@Component({
    selector: 'app-holiday',
    templateUrl: './holiday.component.html',
    styleUrls: ['./holiday.component.scss'],
})
export class HolidayComponent implements OnInit {
    title = 'Neuen Urlaub einrichten';
    toggleOptions = false;

    repeatEvery = ['Nie', 'Jeden Tag', 'Jede Woche', 'Jeden Monat'];
    terminateAfter = ['Nie', 'Nach'];
    repeatModul: string;
    terminateModul: string;


    errorMsg: string;
    successMsg: string;
    holiday: Holiday = new Holiday();
    trainer: Trainer = new Trainer();
    dateTimeParser: DateTimeParserService;

    startDate: NgbDateStruct;
    startTime: NgbTimeStruct;
    endDate: NgbDateStruct;
    endTime: NgbTimeStruct;

    constructor(
        private holidayClient: HolidayClient,
        dateTimeParser: DateTimeParserService
    ) {
        this.dateTimeParser = dateTimeParser;
        this.startTime = { hour: 13, minute: 30, second: 0};
        this.endTime = { hour: 14, minute: 30, second: 0};
        this.repeatModul = this.repeatEvery[0];
        this.terminateModul = this.terminateAfter[0];
    }

    ngOnInit() {}

    public postHoliday(form: NgForm): void {
        console.log('Pass Form Data To Rest Client');
        this.clearInfoMsg();
        this.holiday.id = null;
        this.trainer.id = 1;
        this.holiday.trainer = this.trainer;

        this.holiday.holidayStart = this.dateTimeParser.dateTimeToString(
            this.startDate,
            this.startTime
        );
        this.holiday.holidayEnd = this.dateTimeParser.dateTimeToString(
            this.endDate,
            this.endTime
        );

        this.holidayClient.postNewHoliday(this.holiday).subscribe(
            (data: Holiday) => {
                console.log(data);
                this.successMsg = 'Der Urlaub wurde erfolgreich gespeichert';
            },
            (error: Error) => {
                console.log(error);
                this.errorMsg = error.message;
            }
        );
    }

    public clearInfoMsg(): void {
        this.errorMsg = undefined;
        this.successMsg = undefined;
    }

    public isCompleted(): boolean {
        if (this.startDate === undefined) {
            return false;
        }
        if (this.endDate === undefined) {
            return false;
        }
        return true;
    }

    public isTerminate(): boolean {
        if (this.terminateModul === 'Nie') {
            return false;
        }
        return true;
    }

    public isRepeat(): boolean {
        if (this.repeatModul === 'Nie') {
            return false;
        }
        return true;
    }
    public togg(): void {
        if (this.toggleOptions === false) {
            this.toggleOptions = true;
        } else {
            this.toggleOptions = false;
            this.repeatModul = this.repeatEvery[0];
            this.terminateModul = this.terminateAfter[0];
        }
    }
}
