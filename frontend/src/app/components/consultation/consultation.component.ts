import { Component, OnInit } from '@angular/core';
import { Event } from 'src/app/models/event';
import { Customer } from 'src/app/models/customer';
import { RoomUse } from 'src/app/models/roomUse';
import { NgForm } from '@angular/forms';
import { Room } from 'src/app/models/enum/room';
import { Trainer } from 'src/app/models/trainer';
import { EventType } from 'src/app/models/enum/eventType';
import { EventClient } from 'src/app/rest/event-client';
import { TrainerClient } from 'src/app/rest/trainer-client';
import { HttpResponse } from '@angular/common/http';

import {
    NgbDateParserFormatter,
    NgbDateStruct,
    NgbTimeStruct,
} from '@ng-bootstrap/ng-bootstrap';
import { ClickedDateService } from 'src/app/services/clicked-date.service';
import { DateTimeParserService } from 'src/app/services/date-time-parser.service';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
    selector: 'app-consultation',
    templateUrl: './consultation.component.html',
    styleUrls: ['./consultation.component.scss'],
})
export class ConsultationComponent implements OnInit {
    private roomUse: RoomUse = new RoomUse();
    private trainer: Trainer = new Trainer();
    private dateTimeParser: DateTimeParserService;

    event: Event = new Event();
    customer: Customer = new Customer();
    trainers: Trainer[] = [];

    errorMsg: string;
    successMsg: string;

    startDate: NgbDateStruct;
    startTime: NgbTimeStruct;
    endDate: NgbDateStruct;
    endTime: NgbTimeStruct;

    title = 'Beratungstermin eintragen';
    trainerString = 'Trainer auswählen';
    roomString = 'Raum auswählen';
    rooms: string[] = ['Grün', 'Orange', 'Erdgeschoss'];
    minuteStep = 15;

    constructor(
        private trainerClient: TrainerClient,
        private eventClient: EventClient,
        dateTimeParser: DateTimeParserService,
        private parserFormatter: NgbDateParserFormatter,
        private clickedDateService: ClickedDateService,
        public auth: AuthenticationService
    ) {
        this.dateTimeParser = dateTimeParser;
        const date = this.clickedDateService.getDate();
        const time = this.clickedDateService.getTime();

        this.startTime = this.clickedDateService.getTime();
        this.startDate = this.clickedDateService.getDate();

        this.endDate = this.startDate;
        this.endTime = { hour: this.startTime.hour + 1, minute: 0, second: 0 };
    }

    ngOnInit() {
        this.trainerClient.getAll().subscribe(
            (list: Trainer[]) => {
                this.trainers = list;
            },
            (error) => {
                console.log(error);
            }
        );
    }

    public postConsultation(form: NgForm): void {
        if (this.roomString === 'Raum auswählen') {
            this.errorMsg = 'Ein Raum muss ausgewählt werden';
            return;
        }
        if (this.trainerString === 'Trainer auswählen') {
            this.errorMsg = 'Ein/e Trainer/in muss ausgewählt werden';
            return;
        }
        this.endDate = this.startDate;
        this.endTime = { hour: this.startTime.hour + 1, minute: 0, second: 0 };
        this.roomUse.begin = this.dateTimeParser.dateTimeToString(
            this.startDate,
            this.startTime
        );
        this.roomUse.end = this.dateTimeParser.dateTimeToString(
            this.endDate,
            this.endTime
        );
        this.roomUse.room = this.roomSelected(this.roomString);

        this.event.customerDtos = [this.customer];
        this.event.roomUses = [this.roomUse];
        this.event.eventType = EventType.Consultation;
        this.event.trainer = this.trainer;

        this.eventClient.postNewEvent(this.event).subscribe(
            (data: Event) => {
                this.successMsg =
                    'Deine Reservierung wurde erfolgreich gespeichert';
            },
            (error) => {
                this.errorMsg =
                    'Deine Reservierung konnte nicht angelegt werden: ' +
                    error.message;
            }
        );
    }

    public goBack(): void {
        window.history.back();
    }

    public changeSortOrderRoom(room: string): void {
        this.roomString = room;
    }

    public changeSortOrderTrainer(trainer: Trainer): void {
        this.trainerString = trainer.firstName + ' ' + trainer.lastName;
        this.trainer = trainer;
    }

    private roomSelected(trainer: string): Room {
        if (this.roomString === 'Grün') {
            return Room.Green;
        }
        if (this.roomString === 'Orange') {
            return Room.Orange;
        }
        if (this.roomString === 'Erdgeschoss') {
            return Room.GroundFloor;
        }
        return undefined;
    }

    public isCompleted(): boolean {
        if (
            this.customer.firstName === undefined ||
            this.customer.firstName === ''
        ) {
            return false;
        }
        if (
            this.customer.lastName === undefined ||
            this.customer.lastName === ''
        ) {
            return false;
        }
        if (this.customer.email === undefined || this.customer.email === '') {
            return false;
        }
        if (this.customer.phone === undefined || this.customer.phone === '') {
            return false;
        }
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
        if (this.trainer === undefined || this.trainer === null) {
            return false;
        }
        return true;
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

    public clearInfoMsg(): void {
        this.errorMsg = undefined;
        this.successMsg = undefined;
    }
}
