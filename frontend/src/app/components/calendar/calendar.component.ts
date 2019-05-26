import { Component, OnInit } from '@angular/core';
import { registerLocaleData, DatePipe } from '@angular/common';
import localeDe from '@angular/common/locales/de-AT';

import { CalendarDateFormatter, CalendarView } from 'angular-calendar';

import { CustomDateFormatter } from './CustomDateFormatter';
import { Event } from './Event';
import { BREAKPOINTS } from 'src/app/utils/Breakpoints';

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
    view = CalendarView.Week;
    viewDate = new Date();
    weekStartsOn = 1;
    navButtonLabel: { prev: string; next: string };

    constructor() {
        if (screen.width < BREAKPOINTS.medium) {
            this.daysInWeek = 3;
        } else if (screen.width < BREAKPOINTS.small) {
            this.daysInWeek = 1;
        }
        this.updateNavButtonLabel();
    }

    ngOnInit() {}

    updateNavButtonLabel() {
        console.log('label getter');
        const label = { prev: 'Vorherige', next: 'Nächste' };

        if (this.view === this.calendarView.Month || this.daysInWeek === 1) {
            label.prev = 'Vorheriger';
            label.next = 'Nächster';
        }
        this.navButtonLabel = label;
    }

    toggleView(view: CalendarView, daysInWeek: null | number) {
        this.view = view;
        this.daysInWeek = daysInWeek;
        this.updateNavButtonLabel();
    }
}
