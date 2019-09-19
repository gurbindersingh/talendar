import { Component, OnInit } from '@angular/core';

import { ConsultationTime } from 'src/app/models/consulationTime';
import { ConsultationTimes } from 'src/app/models/consultationTimes';
import { ConsultationTimeClient } from 'src/app/rest/consultationTime-client';
import { ConsultationTimesClient } from 'src/app/rest/consultationTimes-client';

import {
    NgbDateParserFormatter,
    NgbDateStruct,
    NgbTimeStruct,
} from '@ng-bootstrap/ng-bootstrap';
import { NgForm } from '@angular/forms';
import { Trainer } from 'src/app/models';
import {
    DateTimeParserService,
    CronMakerService,
    SessionStorageService,
} from 'src/app/services';

@Component({
    selector: 'app-consultation-time',
    templateUrl: './consultation-time.component.html',
    styleUrls: ['./consultation-time.component.scss'],
})
export class ConsultationTimeComponent implements OnInit {
    title = 'Beratungs Settings';
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
    consultationTimeName: string;
    consultationTimeDescription: string;
    alleX: number;
    endedX: number;

    errorMsg: string;
    successMsg: string;
    consultationTime: ConsultationTime = new ConsultationTime();
    consultationTimes: ConsultationTimes = new ConsultationTimes();
    trainer: Trainer = new Trainer();
    dateTimeParser: DateTimeParserService;
    cronMaker: CronMakerService;

    startDate: NgbDateStruct;
    startTime: NgbTimeStruct;
    endDate: NgbDateStruct;
    endTime: NgbTimeStruct;

    //for price change
    errorMsg2: string;
    successMsg2: string;

    constructor(
        private consultationTimeClient: ConsultationTimeClient,
        private consultationTimesClient: ConsultationTimesClient,
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

    ngOnInit() { }

    public postConsultationTime(form: NgForm): void {
        this.clearInfoMsg();
        this.consultationTime.id = null;
        this.trainer.id = this.sessionService.userId;
        this.consultationTime.trainer = this.trainer;
        this.consultationTimes.trainerid = this.sessionService.userId;

        this.consultationTime.title = this.consultationTimeName;
        this.consultationTimes.title = this.consultationTimeName;
        this.consultationTime.description = this.consultationTimeDescription;
        this.consultationTimes.description = this.consultationTimeDescription;

        this.consultationTime.consultingTimeStart = this.dateTimeParser.dateTimeToString(
            this.startDate,
            this.startTime
        );
        this.consultationTime.consultingTimeEnd = this.dateTimeParser.dateTimeToString(
            this.endDate,
            this.endTime
        );
        if (!this.toggleOptions) {
            this.consultationTimeClient
                .postNewConsultationTime(this.consultationTime)
                .subscribe(
                    (data: ConsultationTime) => {
                        this.resetFormular();
                        this.successMsg =
                            'Die Beratungszeit wurde erfolgreich gespeichert';
                        this.errorMsg = '';
                    },
                    (error: Error) => {
                        this.errorMsg = error.message;
                        this.successMsg = '';
                    }
                );
        } else {
            this.consultationTimes.cronExpression = this.getCron();
            this.consultationTimesClient
                .postNewConsultationTimes(this.consultationTimes)
                .subscribe(
                    (data: ConsultationTime[]) => {
                        this.successMsg =
                            'Die Beratungszeiten wurde erfolgreich gespeichert';
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
        this.consultationTimeName = undefined;
        this.consultationTimeDescription = undefined;
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
        if (this.consultationTimeName === undefined) {
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

    public updatePrice(): void {

    }
}
