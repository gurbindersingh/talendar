import { Component, OnInit } from '@angular/core';
import { Event } from 'src/app/models/event';
import { Customer } from 'src/app/models/customer';
import { RoomUse } from 'src/app/models/roomUse';
import { NgForm } from '@angular/forms';
import { Room } from 'src/app/models/enum/room';
import { Trainer } from 'src/app/models/trainer';
import { EventType } from 'src/app/models/enum/eventType';
import { EventClient, TrainerClient } from 'src/app/rest';

import { NgbDateParserFormatter, NgbDateStruct, NgbTimeStruct } from '@ng-bootstrap/ng-bootstrap';
import { ClickedDateService, DateTimeParserService, AuthenticationService } from 'src/app/services';

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

    selectedTrainer = 'Trainer auswählen';
    selectedRoom = 'Raum auswählen';
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
                
            }
        );
    }

    public postConsultation(form: NgForm): void {
        if (this.selectedRoom === 'Raum auswählen') {
            this.errorMsg = 'Bitte wählen Sie einen Raum aus';
            return;
        }
        if (this.selectedTrainer === 'Trainer auswählen') {
            this.errorMsg = 'Bitte wählen Sie eine/n Trainer/in aus';
            return;
        }
        if (!this.auth.isLoggedIn && window.grecaptcha.getResponse().length < 1) {
            this.errorMsg = 'Bitte schließen Sie das reCaptcha ab.';
            return;
        }

        this.endDate = this.startDate;
        this.endTime = { hour: this.startTime.hour + 1, minute: 0, second: 0 };
        this.roomUse.begin = this.dateTimeParser.dateTimeToString(this.startDate, this.startTime);
        this.roomUse.end = this.dateTimeParser.dateTimeToString(this.endDate, this.endTime);
        this.roomUse.room = this.roomSelected(this.selectedRoom);

        this.event.customerDtos = [this.customer];
        this.event.roomUses = [this.roomUse];
        this.event.eventType = EventType.Consultation;
        this.event.trainer = this.trainer;

        this.eventClient.postNewEvent(this.event).subscribe(
            (data: Event) => {
                this.successMsg = 'Deine Reservierung wurde erfolgreich gespeichert';
            },
            (error) => {
                this.errorMsg = 'Deine Reservierung konnte nicht angelegt werden: ' + error.message;
            }
        );
    }

    public goBack(): void {
        window.history.back();
    }

    public changeSortOrderRoom(room: string): void {
        this.selectedRoom = room;
    }

    public changeSortOrderTrainer(trainer: Trainer): void {
        this.selectedTrainer = trainer.firstName + ' ' + trainer.lastName;
        this.trainer = trainer;
    }

    private roomSelected(trainer: string): Room {
        if (this.selectedRoom === 'Grün') {
            return Room.Green;
        }
        if (this.selectedRoom === 'Orange') {
            return Room.Orange;
        }
        if (this.selectedRoom === 'Erdgeschoss') {
            return Room.GroundFloor;
        }
        return undefined;
    }

    public isCompleted(): boolean {
        if (this.customer.firstName === undefined || this.customer.firstName === '') {
            return false;
        }
        if (this.customer.lastName === undefined || this.customer.lastName === '') {
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
        if (this.selectedRoom === 'Raum auswählen') {
            return false;
        }
        if (this.selectedTrainer === 'Trainer auswählen') {
            return false;
        }
        return true;
    }

    private dateToString(date: NgbDateStruct, time: NgbTimeStruct) {
        let minutes = time.minute.toString();
        let hours = time.hour.toString();

        if (time.minute < 10) {
            minutes = '0' + minutes;
        }
        if (time.hour < 10) {
            hours = '0' + hours;
        }

        return this.parserFormatter.format(date) + 'T' + hours + ':' + minutes + ':00';
    }

    public clearInfoMsg(): void {
        this.errorMsg = undefined;
        this.successMsg = undefined;
    }
}
