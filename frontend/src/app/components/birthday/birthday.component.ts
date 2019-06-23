import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { Event } from '../../models/event';
import { EventClient } from '../../rest/event-client';
import { RoomUse } from 'src/app/models/roomUse';
import { Customer } from 'src/app/models/customer';
import { EventType } from '../../models/enum/eventType';
import { AuthenticationService } from 'src/app/services/authentication.service';
import {
    NgbDateStruct,
    NgbTimeStruct,
    NgbDateParserFormatter,
} from '@ng-bootstrap/ng-bootstrap';
import { NgbDateParserFormatterImplementation } from 'src/app/services/parserformatter.service';
import { NgbDateParserFormatterImplementationUser } from 'src/app/services/parserformatterUser.service';
import { DateTimeParserService } from 'src/app/services/date-time-parser.service';
import { ClickedDateService } from 'src/app/services/clicked-date.service';

@Component({
    selector: 'app-birthday',
    templateUrl: './birthday.component.html',
    styleUrls: ['./birthday.component.scss'],
    providers: [
        {
            provide: NgbDateParserFormatter,
            useClass: NgbDateParserFormatterImplementationUser,
        },
    ],
})
export class BirthdayComponent implements OnInit {
    public ageList: number[] = [
        5,
        6,
        7,
        8,
        9,
        10,
        11,
        12,
        13,
        14,
        15,
        16,
        17,
        18,
        19,
    ];
    public ageListb: number[] = [5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15];
    event: Event = new Event();
    room: RoomUse = new RoomUse();
    customer: Customer = new Customer();
    private date: Date = new Date();

    loading: boolean;
    errorMsg: string;
    successMsg: string;
    startDate: NgbDateStruct = {
        year: this.date.getUTCFullYear(),
        month: this.date.getUTCMonth() + 1,
        day: this.date.getUTCDay(),
    };
    startTime: NgbTimeStruct = { hour: 13, minute: 30, second: 0 };
    private parserFormatter = new NgbDateParserFormatterImplementation();

    constructor(
        private eventClient: EventClient,
        private clickedDateService: ClickedDateService,
        public auth: AuthenticationService
    ) {
        const date = this.clickedDateService.getDate();
        const time = this.clickedDateService.getTime();

        this.startTime = this.clickedDateService.getTime();
        this.startDate = this.clickedDateService.getDate();
    }

    ngOnInit() {
        this.loading = false;
        this.successMsg = '';
        this.errorMsg = '';
    }

    postBirthday(form: NgForm) {
        this.room.begin = this.dateToString(this.startDate, this.startTime);
        this.startTime.hour = this.getEndDate(this.startTime);
        this.room.end = this.dateToString(this.startDate, this.startTime);
        this.event.eventType = EventType.Birthday;
        this.customer.events = null;
        this.room.event = null;
        const roomUses: RoomUse[] = [this.room];
        const customers: Customer[] = [this.customer];
        this.event.roomUses = roomUses;
        this.event.customerDtos = customers;
        this.loading = true;
        this.event.name =
            this.event.birthdayType +
            ' Geburtstag für ' +
            this.customer.firstName +
            ' ' +
            this.customer.lastName +
            ' am ' +
            this.event.roomUses[0].begin;
        
        this.eventClient.postNewEvent(this.event).subscribe(
            (data: Event) => {
                
                this.loading = false;
                this.successMsg = 'Geburtstag wurde erfolgreich gebucht';
                this.errorMsg = '';
                this.clearFormular();
            },
            (error) => {
                
                this.loading = false;
                this.errorMsg = '' + error.message;
                this.successMsg = '';
            }
        );
    }

    public goBack(): void {
        window.history.back();
    }

    public getEndDate(date: NgbTimeStruct): number {
        return (date.hour + 3) % 24;
    }

    public isComplete(): boolean {
        if (this.event.birthdayType === '') {
            return false;
        }
        if (this.event.headcount == null) {
            return false;
        }
        if (this.event.ageToBe == null) {
            return false;
        }
        if (this.customer.firstName === '') {
            return false;
        }
        if (this.customer.lastName === '') {
            return false;
        }
        if (this.customer.email === '') {
            return false;
        }
        if (this.customer.phone === '') {
            return false;
        }
        if (this.startDate == null) {
            return false;
        }
        if (this.startTime == null) {
            return false;
        }
        if (this.room.room == null) {
            return false;
        }
        return true;
    }

    private clearFormular(): void {
        this.customer.firstName = '';
        this.customer.lastName = '';
        this.customer.email = '';
        this.customer.phone = '';
        this.room.room = null;
        this.event.headcount = null;
        this.event.birthdayType = null;
        this.event.ageToBe = null;
    }

    private dateToString(date: NgbDateStruct, time: NgbTimeStruct) {
        let stringMinute = '';
        let stringHour = '';
        let isChangedHour = false;
        let isChangedMinute = false;

        if (time.minute === 1) {
            stringMinute = '01';
            isChangedMinute = true;
        }
        if (time.minute === 2) {
            stringMinute = '02';
            isChangedMinute = true;
        }
        if (time.minute === 3) {
            stringMinute = '03';
            isChangedMinute = true;
        }
        if (time.minute === 4) {
            stringMinute = '04';
            isChangedMinute = true;
        }
        if (time.minute === 5) {
            stringMinute = '05';
            isChangedMinute = true;
        }
        if (time.minute === 6) {
            stringMinute = '06';
            isChangedMinute = true;
        }
        if (time.minute === 7) {
            stringMinute = '07';
            isChangedMinute = true;
        }
        if (time.minute === 8) {
            stringMinute = '08';
            isChangedMinute = true;
        }
        if (time.minute === 9) {
            stringMinute = '09';
            isChangedMinute = true;
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
}
