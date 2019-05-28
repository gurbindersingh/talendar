import { Component, OnInit } from '@angular/core';
import { Event } from 'src/app/models/event';
import { NgForm } from '@angular/forms';
import { EventClient } from 'src/app/rest/event-client';
import { DateTimeParserService } from 'src/app/services/date-time-parser.service';
import { Room } from 'src/app/models/enum/room';
import { EventType } from 'src/app/models/enum/eventType';
import { NgbDateStruct, NgbTimeStruct } from '@ng-bootstrap/ng-bootstrap';
import { RoomUse } from 'src/app/models/roomUse';
import { Trainer } from 'src/app/models/trainer';

@Component({
    selector: 'app-course',
    templateUrl: './course.component.html',
    styleUrls: ['./course.component.scss'],
})
export class CourseComponent implements OnInit {
    title = 'Kurs erstellen';
    minuteStep = 15;
    description = '';
    maxParticipants: number;
    price: number;
    minAge: number;
    maxAge: number;
    startDate: NgbDateStruct;
    startTime: NgbTimeStruct;
    endTime: NgbTimeStruct;
    endOfApplicationDate: NgbDateStruct;
    endOfApplicationTime: NgbTimeStruct;

    greenRadioButton: RadioNodeList;
    radioButtonSelected = '';

    private event: Event = new Event();
    private roomUse: RoomUse = new RoomUse();
    private trainer: Trainer = new Trainer();
    private dateTimeParser: DateTimeParserService;

    private errorMsg: string;
    private successMsg: string;

    constructor(
        private eventClient: EventClient,
        dateTimeParser: DateTimeParserService
    ) {
        this.dateTimeParser = dateTimeParser;
        this.startTime = { hour: 13, minute: 0, second: 0 };
        this.endTime = { hour: 14, minute: 0, second: 0 };
        this.endOfApplicationTime = { hour: 13, minute: 0, second: 0 };
    }

    ngOnInit() {}

    public postMeeting(form: NgForm): void {
        this.event.description = this.description;
        this.event.price = this.price;
        this.event.maxParticipants = this.maxParticipants;
        this.event.minAge = this.minAge;
        this.event.maxAge = this.maxAge;
        this.trainer.id = 1;
        this.event.eventType = EventType.Course;
        this.event.trainer = this.trainer;

        this.roomUse.begin = this.dateTimeParser.dateTimeToString(
            this.startDate,
            this.startTime
        );
        this.roomUse.end = this.dateTimeParser.dateTimeToString(
            this.startDate,
            this.endTime
        );
        this.roomUse.room = this.getSelectedRadioButtonRoom();

        this.event.endOfApplication = this.dateTimeParser.dateTimeToString(
            this.endOfApplicationDate,
            this.endOfApplicationTime
        );

        this.event.roomUses = [this.roomUse];

        this.eventClient.postNewEvent(this.event).subscribe(
            (data: Event) => {
                console.log(data);
                this.successMsg =
                    'Deine Reservierung wurde erfolgreich gespeichert';
            },
            (error: Error) => {
                console.log(error);
                this.errorMsg = error.message;
            }
        );
    }

    public isCompleted(): boolean {
        if (this.event.name === undefined || this.event.name === '') {
            return false;
        }
        if (this.description === undefined || this.description === '') {
            return false;
        }
        if (this.price === undefined) {
            return false;
        }
        if (this.maxParticipants === undefined) {
            return false;
        }
        if (this.minAge === undefined) {
            return false;
        }
        if (this.maxAge === undefined) {
            return false;
        }
        if (this.radioButtonSelected === '') {
            return false;
        }
        return true;
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
        return Room.Groundfloor;
    }

    public clearInfoMsg(): void {
        this.errorMsg = undefined;
        this.successMsg = undefined;
    }
}
