import { Component, OnInit } from '@angular/core';
import { registerLocaleData, DatePipe } from '@angular/common';
import localeDe from '@angular/common/locales/de-AT';

import { CalendarDateFormatter } from 'angular-calendar';

import { CustomDateFormatter } from './CustomDateFormatter';
import { Event } from './Event';
import { BREAKPOINTS } from 'src/utils/Breakpoints';
import { CalendarView } from './CalendarView';

/**
 * In order to display week days in German the locale data
 * needs to be imported and registered, since the calender
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
    daysInWeek: number | null = null;
    dayStartHour = 8;
    dayStartMinute = 0;
    events: Event[] = [];
    hourSegments = 4;
    locale = 'de-AT';
    precision = 'minutes';
    viewDate = new Date();
    view = CalendarView.Week;
    weekStartsOn = 1;

    constructor() {
        if (screen.width < BREAKPOINTS.medium) {
            this.daysInWeek = 3;
        }
        this.view = this.calendarView.Week;
    }

    ngOnInit() {}
}
