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
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'app-course',
    templateUrl: './course.component.html',
    styleUrls: ['./course.component.scss'],
})
export class CourseComponent implements OnInit {
    title = 'Kurs eintragen';
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
    isCreate: boolean;

    btnText: string;
    saveMode: boolean;
    valueEndOfApplication: NgbDateStruct;

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
        dateTimeParser: DateTimeParserService,
        private route: ActivatedRoute
    ) {
        this.dateTimeParser = dateTimeParser;
        this.startTime = { hour: 13, minute: 0, second: 0 };
        this.endTime = { hour: 14, minute: 0, second: 0 };
        this.endOfApplicationTime = { hour: 13, minute: 0, second: 0 };
    }

    ngOnInit() {
        const id: number = this.route.snapshot.queryParams.id;

        if (id === undefined) {
            this.title = 'Kurs eintragen';
            this.btnText = 'Erstellen';
            this.saveMode = true;
            this.isCreate = true;
        } else {
            this.title = 'Kurs bearbeiten';
            this.btnText = 'Bearbeiten';
            this.saveMode = false;
            this.isCreate = false;
            this.eventClient.getEventById(id).subscribe(
                (data: Event) => {
                    console.log(data);
                    this.event = data;
                },
                (error: Error) => {
                    this.errorMsg =
                        'Der ausgewählte Trainer konnte leider nicht geladen werden.';
                }
            );
        }
    }

    public postMeeting(form: NgForm): void {
        this.trainer.id = 1;
        if (this.isCreate) {
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
        } else {
            // TODO
            this.eventClient.update(this.event).subscribe(
                (data: Event) => {
                    console.log(data);
                    this.successMsg = 'Der Kurs wurde erfolgreich aktualisiert';
                },
                (error: Error) => {
                    console.log(error.message);
                    this.errorMsg =
                        'Der Kurs konnte nicht erfolgreich aktualisiert werden: ' +
                        error.message;
                }
            );
        }
    }

    public isCompleted(): boolean {
        if (this.event.name === undefined || this.event.name === '') {
            return false;
        }
        if (
            this.event.description === undefined ||
            this.event.description === ''
        ) {
            return false;
        }
        if (this.event.price === undefined || this.event.price === null) {
            return false;
        }
        if (
            this.event.maxParticipants === undefined ||
            this.event.maxParticipants === null
        ) {
            return false;
        }
        if (this.event.minAge === undefined || this.event.minAge === null) {
            return false;
        }
        if (this.event.maxAge === undefined || this.event.maxAge === null) {
            return false;
        }
        if (this.isCreate) {
            if (this.startDate === undefined) {
                return false;
            }
            if (this.endTime === undefined) {
                return false;
            }
            if (this.radioButtonSelected === '') {
                return false;
            }
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
        return Room.GroundFloor;
    }

    public clearInfoMsg(): void {
        this.errorMsg = undefined;
        this.successMsg = undefined;
    }
}
