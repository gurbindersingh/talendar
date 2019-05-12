import { Component, OnInit } from '@angular/core';
import { CalendarEvent } from 'calendar-utils';
import { registerLocaleData } from '@angular/common';
import localeDe from '@angular/common/locales/de-AT';

registerLocaleData(localeDe);

@Component({
    selector: 'app-calendar',
    templateUrl: './calendar.component.html',
    styleUrls: ['./calendar.component.scss'],
})
export class CalendarComponent implements OnInit {
    private events: CalendarEvent[];
    private today: Date;
    private weekStartsOn: number;
    private dayStartHour: number;
    private dayStartMinute: number;
    private dayEndHour: number;
    private dayEndMinute: number;
    private hourSegments: number;
    private locale: string;
    private precision: string;

    constructor() {
        this.events = [];
        this.today = new Date();
        this.locale = 'de-AT';
        this.precision = 'minutes';
        this.hourSegments = 4;
        this.weekStartsOn = 1;
        this.dayStartHour = 8;
        this.dayStartMinute = 0;
        this.dayEndHour = 20;
        this.dayEndMinute = 0;
    }

    ngOnInit() {}
}
