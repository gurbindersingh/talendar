import { Component, OnInit } from '@angular/core';
import { Holiday} from 'src/app/models/holiday';
import {NgbDateParserFormatter, NgbDateStruct, NgbTimeStruct} from '@ng-bootstrap/ng-bootstrap';
import { NgForm } from '@angular/forms';
import { Trainer } from 'src/app/models/trainer';
import { HolidayClient } from 'src/app/rest/holiday-client';

@Component({
  selector: 'app-holiday',
  templateUrl: './holiday.component.html',
  styleUrls: ['./holiday.component.scss']
})

export class HolidayComponent implements OnInit {
    title = 'Neuen Urlaub einrichten';
    stime = {hour: 13, minute: 30};
    etime = {hour: 14, minute: 30};

    private errorMsg: string;
    private successMsg: string;
    private holiday: Holiday = new Holiday();
    private trainer: Trainer = new Trainer();

    startDate: NgbDateStruct;
    startTime: NgbTimeStruct;
    endDate: NgbDateStruct;
    endTime: NgbTimeStruct;

    constructor(
        private holidayClient: HolidayClient,
        private parserFormatter: NgbDateParserFormatter) {
    }

    ngOnInit() {
    }

    public postHoliday(form: NgForm): void {
        console.log('Pass Form Data To Rest Client');
        this.clearInfoMsg();
        this.holiday.id = null;
        this.trainer.id = 1;
        this.holiday.trainer = this.trainer;

        this.holiday.holidayStart = this.dateToString(this.startDate, this.startTime);
        this.holiday.holidayEnd = this.dateToString(this.endDate, this.endTime);

        this.holidayClient.postNewHoliday(this.holiday).subscribe(
            (data: Holiday) => {
                console.log(data);
                this.successMsg = 'Der Urlaub wurde erfolgreich gespeichert';
            },
            (error) => {
                console.log(error);
                this.errorMsg = 'Der Urlaub konnte nicht angelegt werden!';
            }
        );
    }

    public clearInfoMsg(): void {
      this.errorMsg = undefined;
      this.successMsg = undefined;
    }

    private dateToString(date: NgbDateStruct, time: NgbTimeStruct) {
        let stringMinute = '';
        let stringHour = '';
        let isChangedHour = false;
        let isChangedMinute = false;

        if (time.minute === 0) {
            stringMinute = '00';
            isChangedMinute = true;
        }
        if (time.hour === 0) {
            stringHour = '00';
            isChangedHour = true;
        }
        if (time.hour === 1) {
            stringHour = '01';
            isChangedHour = true;
        }
        if (time.hour === 2) {
            stringHour = '02';
            isChangedHour = true;
        }
        if (time.hour === 3) {
            stringHour = '03';
            isChangedHour = true;
        }
        if (time.hour === 4) {
            stringHour = '04';
            isChangedHour = true;
        }
        if (time.hour === 5) {
            stringHour = '05';
            isChangedHour = true;
        }
        if (time.hour === 6) {
            stringHour = '06';
            isChangedHour = true;
        }
        if (time.hour === 7) {
            stringHour = '07';
            isChangedHour = true;
        }
        if (time.hour === 8) {
            stringHour = '08';
            isChangedHour = true;
        }
        if (time.hour === 9) {
            stringHour = '09';
            isChangedHour = true;
        }

        if (isChangedHour && isChangedMinute) {
            return (
                this.parserFormatter.format(date) +
                'T' +
                stringHour +
                ':' +
                stringMinute +
                ':00'
            );
        }

        if (isChangedHour) {
            return (
                this.parserFormatter.format(date) +
                'T' +
                stringHour +
                ':' +
                time.minute +
                ':00'
            );
        }

        if (isChangedMinute) {
            return (
                this.parserFormatter.format(date) +
                'T' +
                time.hour +
                ':' +
                time.minute +
                ':00'
            );
        }

        return (
            this.parserFormatter.format(date) +
            'T' +
            time.hour +
            ':' +
            time.minute +
            ':00'
        );
    }

    public isCompleted(): boolean {
        if (this.startDate === undefined) {
            return false;
        }
        if (this.startTime === undefined) {
            return false;
        }
        if (this.endDate === undefined) {
            return false;
        }
        if (this.endTime === undefined) {
            return false;
        }
        return true;
    }
}
