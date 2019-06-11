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
import { CronMakerService } from 'src/app/services/cronMaker.service';

@Component({
    selector: 'app-course',
    templateUrl: './course.component.html',
    styleUrls: ['./course.component.scss'],
})
export class CourseComponent implements OnInit {
    title = 'Kurs eintragen';

    cronMaker: CronMakerService;

    roomOption = 0;
    otherRoom1String: string;
    otherRoom2String: string;

    repeatEvery = ['Nie', 'Jeden Tag', 'Jede Woche', 'Jeden Monat'];
    terminateAfter = ['Nie', 'Nach'];
    repeatModul: string;
    terminateModul: string;
    alleX: number;
    endedX: number;

    loading = false;

    minuteStep = 15;
    description = '';
    toggleOptions = false;
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
        private route: ActivatedRoute,
        cronMaker: CronMakerService
    ) {
        this.cronMaker = cronMaker;
        this.dateTimeParser = dateTimeParser;
        this.startTime = { hour: 13, minute: 0, second: 0 };
        this.endTime = { hour: 14, minute: 0, second: 0 };
        this.endOfApplicationTime = { hour: 13, minute: 0, second: 0 };
        this.repeatModul = this.repeatEvery[0];
        this.terminateModul = this.terminateAfter[0];
        this.alleX = 1;
        this.endedX = 1;
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

    public roomOption0Clicked(): void {
        this.roomOption = 0;
    }

    public roomOption1Clicked(): void {
        this.roomOption = 1;
    }

    public roomOption2Clicked(): void {
        this.roomOption = 2;
    }

    public roomOption3Clicked(): void {
        this.roomOption = 3;
    }

    public roomOption4Clicked(): void {
        this.roomOption = 4;
    }

    public isRepeat(): boolean {
        if (this.repeatModul === 'Nie') {
            return false;
        }
        return true;
    }

    public togg(): void {
        if (this.toggleOptions === false) {
            this.toggleOptions = true;
        } else {
            this.toggleOptions = false;
            this.repeatModul = this.repeatEvery[0];
            this.terminateModul = this.terminateAfter[0];
        }
    }

    public isTerminate(): boolean {
        if (this.terminateModul === 'Nie') {
            return false;
        }
        return true;
    }

    public getCron(): string {
        return this.cronMaker.createCron(
            this.startDate,
            this.startTime,
            this.startDate,
            this.endTime,
            this.toggleOptions,
            this.repeatModul,
            this.alleX,
            this.terminateModul,
            this.endedX
        );
    }

    public postMeeting(form: NgForm): void {
        if (this.isCreate) {
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

            if (this.toggleOptions) {
                this.roomUse.cronExpression = this.getCron();
                this.roomUse.roomOption = this.roomOption;
            }
            this.event.roomUses = [this.roomUse];

            this.loading = true;
            this.eventClient.postNewEvent(this.event).subscribe(
                (data: Event) => {
                    console.log(data);
                    this.successMsg =
                        'Deine Reservierung wurde erfolgreich gespeichert';
                    this.errorMsg = '';
                    this.loading = false;
                },
                (error: Error) => {
                    console.log(error);
                    this.errorMsg = error.message;
                    this.successMsg = '';
                    this.loading = false;
                }
            );
        } else {
            // TODO
            this.eventClient.update(this.event).subscribe(
                (data: Event) => {
                    console.log(data);
                    this.successMsg = 'Der Kurs wurde erfolgreich aktualisiert';
                    this.errorMsg = '';
                    this.loading = false;
                },
                (error: Error) => {
                    console.log(error.message);
                    this.errorMsg =
                        'Der Kurs konnte nicht erfolgreich aktualisiert werden: ' +
                        error.message;
                    this.successMsg = '';
                    this.loading = false;
                }
            );
        }
    }

    public isRoomChoosed(): boolean {
        if (this.radioButtonSelected === '') {
            return false;
        }
        return true;
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
        this.otherRoom1String = 'Orange';
        this.otherRoom2String = 'Erdgeschoss';
    }

    public orangeSelected(): void {
        this.radioButtonSelected = 'Orange';
        this.otherRoom1String = 'Grün';
        this.otherRoom2String = 'Erdgeschoss';
    }

    public groundFloorSelected(): void {
        this.radioButtonSelected = 'Erdgeschoss';
        this.otherRoom1String = 'Grün';
        this.otherRoom2String = 'Orange';
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
