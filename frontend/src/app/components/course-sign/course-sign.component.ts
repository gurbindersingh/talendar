import { Component, OnInit } from '@angular/core';
import {
    NgbDateStruct,
    NgbTimeStruct,
    NgbDate,
} from '@ng-bootstrap/ng-bootstrap';
import { RoomUse } from 'src/app/models/roomUse';
import { Room } from 'src/app/models/enum/room';
import { EventClient } from 'src/app/rest/event-client';
import { ActivatedRoute } from '@angular/router';
import { Event } from 'src/app/models/event';
import { Customer } from 'src/app/models/customer';
import { DateTimeParserService } from 'src/app/services/date-time-parser.service';
import { NgForm } from '@angular/forms';

@Component({
    selector: 'app-course-sign',
    templateUrl: './course-sign.component.html',
    styleUrls: ['./course-sign.component.scss'],
})
export class CourseSignComponent implements OnInit {
    private id: number;
    private event: Event = new Event();
    private customer: Customer = new Customer();
    private customerLengthBeforeUpdate: number;

    canSignIn: boolean;
    wantsEmail = false;

    private errorMsg: string;
    private successMsg: string;

    loading: boolean;

    btnClicked = false;

    title = 'Kurs anmelden';

    dates: string[];
    times: string[];

    currentPage: number;
    itemsPerPage: number;
    datesListPage: string[] = [];

    date: NgbDateStruct;
    time: NgbTimeStruct;
    birthOfChild: NgbDateStruct;

    stringOfEofDate: string;
    stringOfEofTime: string;
    dateTimeParser: DateTimeParserService;

    constructor(
        private eventClient: EventClient,
        private route: ActivatedRoute,
        dateTimeParser: DateTimeParserService
    ) {
        this.dateTimeParser = dateTimeParser;
    }

    ngOnInit() {
        this.currentPage = 1;
        this.itemsPerPage = 12;
        this.id = this.route.snapshot.queryParams.id;
        this.loading = false;
        this.event.customerDtos = []; // needed
        this.getEventFromBackendById(this.id);
        this.canSignIn = true;
    }

    updateListPage(page?: number): void {
        this.datesListPage = this.dates.slice(
            (this.currentPage - 1) * this.itemsPerPage,
            this.currentPage * this.itemsPerPage
        );
    }

    public scroll(el: HTMLElement) {
        el.scrollIntoView();
    }

    public getEventFromBackendById(id: number): void {
        this.dates = [];
        this.times = [];
        this.eventClient.getEventById(id).subscribe(
            (data: Event) => {
                
                this.event = data;
                this.event.customerDtos = data.customerDtos;
                this.event.roomUses = data.roomUses;
                this.stringOfEofDate = this.extractDate(data.endOfApplication);
                this.stringOfEofTime = this.extractTime(data.endOfApplication);
                this.customerLengthBeforeUpdate = data.customerDtos.length;
                for (const roomUse of this.event.roomUses) {
                    this.pushStringToArray(
                        roomUse.begin,
                        roomUse.end,
                        roomUse.room
                    );
                }
                this.updateListPage();
            },
            (error: Error) => {
                this.errorMsg =
                    'Etwas ist schief gelaufen. Event existiert nicht.';
                this.canSignIn = false;
            }
        );

        
    }

    public updateCourseCustomers(): void {
        this.errorMsg = '';
        this.successMsg = '';
        this.event.customerDtos = [];
        this.customer.id = null;
        this.event.customerDtos.push(this.customer);
        this.loading = true;
        const time = { hour: 0, minute: 0, second: 0 };
        this.customer.wantsEmail = this.wantsEmail;
        this.customer.birthOfChild = this.dateTimeParser.dateTimeToString(
            this.birthOfChild,
            time
        );
        this.eventClient.updateCustomer(this.event).subscribe(
            (data: Event) => {
                
                if (
                    data.customerDtos.length === this.customerLengthBeforeUpdate
                ) {
                    this.btnClicked = false;
                    this.loading = false;
                    this.errorMsg = 'Sie sind schon angemeldet';
                    this.successMsg = '';
                } else {
                    this.btnClicked = true;
                    
                    this.successMsg = 'Sie sind jetzt angemeldet!';
                    this.errorMsg = '';
                }
            },
            (error: Error) => {
                this.btnClicked = false;
                this.loading = false;
                
                this.successMsg = '';
                this.errorMsg = error.message;
            }
        );
        this.getEventFromBackendById(this.id);
        
    }

    public checkBoxClicked() {
        this.wantsEmail = !this.wantsEmail;
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
        if (
            this.customer.childName === undefined ||
            this.customer.childName === ''
        ) {
            return false;
        }
        if (
            this.customer.childLastName === undefined ||
            this.customer.childLastName === ''
        ) {
            return false;
        }
        if (this.birthOfChild === undefined) {
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
