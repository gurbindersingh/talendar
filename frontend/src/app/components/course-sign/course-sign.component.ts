import { Component, OnInit } from '@angular/core';
import { NgbDateStruct, NgbTimeStruct } from '@ng-bootstrap/ng-bootstrap';
import { RoomUse } from 'src/app/models/roomUse';
import { Room } from 'src/app/models/enum/room';
import { EventClient } from 'src/app/rest/event-client';
import { ActivatedRoute } from '@angular/router';
import { Event } from 'src/app/models/event';
import { Customer } from 'src/app/models/customer';
import { NgForm } from '@angular/forms';

@Component({
    selector: 'app-course-sign',
    templateUrl: './course-sign.component.html',
    styleUrls: ['./course-sign.component.scss'],
})
export class CourseSignComponent implements OnInit {
    private event: Event = new Event();
    private customer: Customer = new Customer();

    private errorMsg: string;
    private successMsg: string;

    countCustomer: number;

    title = 'Kurs anmelden';

    dates: string[];
    times: string[];

    date: NgbDateStruct;
    time: NgbTimeStruct;

    constructor(
        private eventClient: EventClient,
        private route: ActivatedRoute
    ) {}

    ngOnInit() {
        const id: number = this.route.snapshot.queryParams.id;

        this.dates = [];
        this.times = [];

        this.eventClient.getEventById(id).subscribe(
            (data: Event) => {
                console.log(data);
                this.event = data;
                this.countCustomer = this.event.customerDtos.length;
                for (const roomUse of this.event.roomUses) {
                    this.pushStringToArray(
                        roomUse.begin,
                        roomUse.end,
                        roomUse.room
                    );
                }
            },
            (error: Error) => {
                this.errorMsg =
                    'Der ausgewählte Trainer konnte leider nicht geladen werden.';
            }
        );
    }

    public updateCourseCustomers(courseSignForm: NgForm): void {
        this.event.customerDtos.push(this.customer);
        this.eventClient.updateCustomer(this.event).subscribe(
            (data: Event) => {
                if (data.customerDtos.length < this.event.customerDtos.length) {
                    this.errorMsg = 'Sie sind schon angemeldet';
                    this.event.customerDtos = data.customerDtos;
                    this.countCustomer = data.customerDtos.length;
                } else {
                    this.countCustomer = data.customerDtos.length;
                    console.log(data);
                    this.successMsg = 'Sie sind jetzt angemeldet!';
                }
            },
            (error: Error) => {
                console.log(error);
                this.errorMsg = error.message;
            }
        );
    }

    public isCompleted(): boolean {
        if (this.customer.phone === undefined || this.customer.phone === '') {
            return false;
        }
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
        return true;
    }

    public pushStringToArray(begin: string, end: string, room: Room): void {
        this.dates.push(
            this.extractDate(begin) +
                ' | ' +
                this.extractTime(begin) +
                '-' +
                this.extractTime(end) +
                ' im Raum ' +
                this.extractRoomToString(room)
        );
    }

    public extractDate(date: string): string {
        const splitted = date.split('T')[0].split('-');
        return splitted[2] + '-' + splitted[1] + '-' + splitted[0];
    }

    public extractTime(date: string): string {
        const splitted = date.split('T')[1].split(':');
        return splitted[0] + ':' + splitted[1];
    }

    public extractRoomToString(room: Room): string {
        if (room === 'Green') {
            return 'Grün';
        }
        if (room === 'Orange') {
            return 'Orange';
        }
        if (room === 'GroundFloor') {
            return 'Erdgeschoss';
        }
        return '';
    }
}
