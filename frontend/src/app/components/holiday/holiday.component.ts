import { Component, OnInit } from '@angular/core';
import { Holiday } from 'src/app/models/holiday';
import {
    NgbDateParserFormatter,
    NgbDateStruct,
    NgbTimeStruct,
} from '@ng-bootstrap/ng-bootstrap';
import { NgForm } from '@angular/forms';
import { Trainer } from 'src/app/models';
import { HolidayClient, HolidaysClient } from 'src/app/rest';
import {
    DateTimeParserService,
    CronMakerService,
    SessionStorageService,
} from 'src/app/services';
import { Holidays } from 'src/app/models';

@Component({
    selector: 'app-holiday',
    templateUrl: './holiday.component.html',
    styleUrls: ['./holiday.component.scss'],
})
export class HolidayComponent implements OnInit {
    title = 'Neuen Urlaub eintragen';
    toggleOptions = false;

    repeatOptions = [
        { label: 'Nie', value: '' },
        { label: 'Jeden Tag', value: 'Tage' },
        { label: 'Jede Woche', value: 'Wochen' },
        { label: 'Jeden Monat', value: 'Monate' },
    ];
    terminateAfterOption = ['Nie', 'Nach'];
    selectedRepeatOption: string;
    terminateModul: string;
    holidayName: string;
    holidayDescription: string;
    alleX: number;
    endedX: number;

    errorMsg: string;
    successMsg: string;
    holiday: Holiday = new Holiday();
    trainer: Trainer = new Trainer();
    holidays: Holidays = new Holidays();
    dateTimeParser: DateTimeParserService;
    cronMaker: CronMakerService;

    startDate: NgbDateStruct;
    startTime: NgbTimeStruct;
    endDate: NgbDateStruct;
    endTime: NgbTimeStruct;

    constructor(
        private holidayClient: HolidayClient,
        private holidaysClient: HolidaysClient,
        dateTimeParser: DateTimeParserService,
        cronMaker: CronMakerService,
        private sessionService: SessionStorageService
    ) {
        this.cronMaker = cronMaker;
        this.dateTimeParser = dateTimeParser;
        this.startTime = { hour: 13, minute: 30, second: 0 };
        this.endTime = { hour: 14, minute: 30, second: 0 };
        this.selectedRepeatOption = this.repeatOptions[0].value;
        this.terminateModul = this.terminateAfterOption[0];
        this.alleX = 1;
        this.endedX = 1;
    }

    ngOnInit() {}

    public postHoliday(form: NgForm): void {
        this.clearInfoMsg();
        this.holiday.id = null;
        this.trainer.id = this.sessionService.userId;
        this.holiday.trainer = this.trainer;
        this.holidays.trainerid = this.sessionService.userId;

        this.holiday.title = this.holidayName;
        this.holidays.title = this.holidayName;
        this.holiday.description = this.holidayDescription;
        this.holidays.description = this.holidayDescription;

        this.holiday.holidayStart = this.dateTimeParser.dateTimeToString(
            this.startDate,
            this.startTime
        );
        this.holiday.holidayEnd = this.dateTimeParser.dateTimeToString(
            this.endDate,
            this.endTime
        );
        if (!this.toggleOptions) {
            this.holidayClient.postNewHoliday(this.holiday).subscribe(
                (data: Holiday) => {
                    this.resetFormular();
                    this.successMsg =
                        'Der Urlaub wurde erfolgreich gespeichert';
                    this.errorMsg = '';
                },
                (error: Error) => {
                    this.errorMsg = error.message;
                    this.successMsg = '';
                }
            );
        } else {
            this.holidays.cronExpression = this.getCron();
            this.holidaysClient.postNewHolidays(this.holidays).subscribe(
                (data: Holiday[]) => {
                    this.successMsg =
                        'Die Urlaube wurde erfolgreich gespeichert';
                    this.errorMsg = '';
                    this.resetFormular();
                },
                (error: Error) => {
                    this.errorMsg = error.message;
                    this.successMsg = '';
                }
            );
        }
    }

    private resetFormular(): void {
        this.holidayName = undefined;
        this.holidayDescription = undefined;
        this.startDate = undefined;
        this.endDate = undefined;
        if (this.toggleOptions) {
            this.togg();
        }
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
        if (this.holidayName === undefined) {
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
        if (this.selectedRepeatOption === 'Nie') {
            return false;
        }
        return true;
    }
    public togg(): void {
        if (this.toggleOptions === false) {
            this.toggleOptions = true;
        } else {
            this.toggleOptions = false;
            this.selectedRepeatOption = this.repeatOptions[0].value;
            this.terminateModul = this.terminateAfterOption[0];
            this.alleX = 1;
            this.endedX = 1;
        }
    }
    public getCron(): string {
        return this.cronMaker.createCron(
            this.startDate,
            this.startTime,
            this.endDate,
            this.endTime,
            this.toggleOptions,
            this.selectedRepeatOption,
            this.alleX,
            this.terminateModul,
            this.endedX
        );
    }
}
