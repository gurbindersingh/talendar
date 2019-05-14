import { Component, OnInit } from '@angular/core';
import { registerLocaleData, DatePipe } from '@angular/common';
import localeDe from '@angular/common/locales/de-AT';

import { CalendarDateFormatter, CalendarView } from 'angular-calendar';
import { CustomDateFormatter } from './CustomDateFormatter';
import { Event } from './Event';

/**
 * In order to display week days in German the locale data
 * needs to be imported and registered, because the calender
 * uses Angular's i18n API.
 */
registerLocaleData(localeDe);

@Component({
    selector: 'app-calendar',
    templateUrl: './calendar.component.html',
    styleUrls: ['./calendar.component.scss'],
    providers: [
        { provide: CalendarDateFormatter, useClass: CustomDateFormatter },
    ],
})
export class CalendarComponent implements OnInit {
    calendarView = CalendarView;
    dayEndHour = 20;
    dayEndMinute = 0;
    daysInWeek = 7;
    dayStartHour = 8;
    dayStartMinute = 0;
    events: Event[] = [];
    hourSegments = 4;
    locale = 'de-AT';
    precision = 'minutes';
    viewDate = new Date();
    view = CalendarView.Week;
    weekStartsOn = 1;

    constructor() {}

    ngOnInit() {}
}
