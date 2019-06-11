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

@Component({
    selector: 'app-consultation',
    templateUrl: './consultation.component.html',
    styleUrls: ['./consultation.component.scss'],
})
export class ConsultationComponent implements OnInit {
    private event: Event = new Event();
    private customer: Customer = new Customer();
    private roomUse: RoomUse = new RoomUse();
    private trainers: Trainer[] = [];
    private trainer: Trainer = new Trainer();

    private errorMsg: string;
    private successMsg: string;

    startDate: NgbDateStruct;
    startTime: NgbTimeStruct = { hour: 13, minute: 30, second: 0 };
    endDate: NgbDateStruct;
    endTime: NgbTimeStruct = { hour: 14, minute: 30, second: 0 };

    title = 'Beratungstermin eintragen';
    trainerString = 'Trainer auswählen';
    roomString = 'Raum auswählen';
    rooms: string[] = ['Grün', 'Orange', 'Erdgeschoss'];
    minuteStep = 15;

    constructor(
        private trainerClient: TrainerClient,
        private eventClient: EventClient,
        private parserFormatter: NgbDateParserFormatter
    ) {}

    ngOnInit() {
        console.log('Init Trainer List');
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
        this.roomUse.begin = this.dateToString(this.startDate, this.startTime);
        this.roomUse.end = this.dateToString(this.endDate, this.endTime);
        this.roomUse.room = this.roomSelected(this.roomString);

        this.event.customerDtos = [this.customer];
        this.event.roomUses = [this.roomUse];
        this.event.eventType = EventType.Consultation;
        this.event.trainer = this.trainer;

        this.eventClient.postNewEvent(this.event).subscribe(
            (data: Event) => {
                console.log(data);
                this.successMsg =
                    'Deine Reservierung wurde erfolgreich gespeichert';
            },
            (error) => {
                console.log(error.message);
                this.errorMsg =
                    'Deine Reservierung konnte nicht angelegt werden: ' +
                    error.message;
            }
        );
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
        if (this.trainer === undefined) {
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
