import { Component, OnInit } from '@angular/core';
import { registerLocaleData, DatePipe } from '@angular/common';
import localeDe from '@angular/common/locales/de-AT';

import {
    CalendarDateFormatter,
    DateFormatterParams,
    CalendarView,
} from 'angular-calendar';
import { CalendarEvent } from 'calendar-utils';

registerLocaleData(localeDe);

class CustomDateFormatter extends CalendarDateFormatter {
    public dayViewHour({ date, locale }: DateFormatterParams): string {
        return new DatePipe(locale).transform(date, 'HH:mm', locale);
    }

    public weekViewHour({ date, locale }: DateFormatterParams): string {
        return this.dayViewHour({ date, locale });
    }
}

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
    events: CalendarEvent[] = [];
    hourSegments = 4;
    locale = 'de-AT';
    precision = 'minutes';
    viewDate = new Date();
    view = CalendarView.Week;
    weekStartsOn = 1;

    constructor() {}

    ngOnInit() {}

    setView(view: CalendarView) {
        this.view = view;
    }
}
