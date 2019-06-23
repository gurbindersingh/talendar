import { Component, OnInit } from '@angular/core';
import { Event } from 'src/app/models/event';
import { Customer } from 'src/app/models/customer';
import { RoomUse } from 'src/app/models/roomUse';
import { NgForm } from '@angular/forms';
import { Room } from 'src/app/models/enum/room';
import { EventType } from 'src/app/models/enum/eventType';
import { EventClient } from 'src/app/rest/event-client';
import { DateTimeParserService } from 'src/app/services/date-time-parser.service';

import { NgbDateStruct, NgbTimeStruct } from '@ng-bootstrap/ng-bootstrap';
import { ClickedDateService } from 'src/app/services/clicked-date.service';

@Component({
    selector: 'app-meeting',
    templateUrl: './rent.component.html',
    styleUrls: ['./rent.component.scss'],
})
export class RentComponent implements OnInit {
    private event: Event = new Event();
    private customer: Customer = new Customer();
    private roomUse: RoomUse = new RoomUse();

    private dateTimeParser: DateTimeParserService;

    private errorMsg: string;
    private successMsg: string;

    greenRadioButton: RadioNodeList;
    loading = false;

    radioButtonSelected = '';

    startDate: NgbDateStruct;
    startTime: NgbTimeStruct;
    endTime: NgbTimeStruct;

    title = 'Raum mieten';
    minuteStep = 15;

    constructor(
        private eventClient: EventClient,
        dateTimeParser: DateTimeParserService,
        private clickedDateService: ClickedDateService
    ) {
        const date = this.clickedDateService.getDate();
        const time = this.clickedDateService.getTime();

        this.dateTimeParser = dateTimeParser;
        this.startTime = this.clickedDateService.getTime();
        this.endTime = { hour: this.startTime.hour + 1, minute: 0, second: 0 };

        this.startDate = this.clickedDateService.getDate();
    }

    ngOnInit() {}

    public postMeeting(form: NgForm): void {
        this.roomUse.begin = this.dateTimeParser.dateTimeToString(
            this.startDate,
            this.startTime
        );
        this.roomUse.end = this.dateTimeParser.dateTimeToString(
            this.startDate,
            this.endTime
        );
        this.roomUse.room = this.getSelectedRadioButtonRoom();

        this.event.customerDtos = [this.customer];
        this.event.roomUses = [this.roomUse];
        this.event.eventType = EventType.Rent;
        this.loading = true;
        this.eventClient.postNewEvent(this.event).subscribe(
            (data: Event) => {
                
                this.successMsg =
                    'Deine Reservierung wurde erfolgreich gespeichert';
                this.errorMsg = '';
                this.loading = false;
                this.resetFormular();
            },
            (error: Error) => {
                
                this.errorMsg = error.message;
                this.successMsg = '';
                this.loading = false;
            }
        );
    }

    private resetFormular(): void {
        this.customer.firstName = '';
        this.customer.lastName = '';
        this.customer.phone = '';
        this.customer.email = '';
    }

    public greenSelected(): void {
        this.radioButtonSelected = 'Grün';
    }

    public orangeSelected(): void {
        this.radioButtonSelected = 'Orange';
    }

    public groundFloorSelected(): void {
        this.radioButtonSelected = 'Erdgeschoss';
    }

    public getSelectedRadioButtonRoom(): Room {
        if (this.radioButtonSelected === 'Grün') {
            return Room.Green;
        }
        if (this.radioButtonSelected === 'Orange') {
            return Room.Orange;
        }
        return Room.GroundFloor;
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
        if (this.startDate === undefined) {
            return false;
        }
        if (this.endTime === undefined) {
            return false;
        }
        if (this.radioButtonSelected === '') {
            return false;
        }
        return true;
    }

    public clearInfoMsg(): void {
        this.errorMsg = undefined;
        this.successMsg = undefined;
    }
}
