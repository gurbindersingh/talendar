import { Component, OnInit } from '@angular/core';
import { CalendarEvent } from 'calendar-utils';
import { registerLocaleData, formatDate, DatePipe } from '@angular/common';
import { CalendarDateFormatter, DateFormatterParams } from 'angular-calendar';
import localeDe from '@angular/common/locales/de-AT';

registerLocaleData(localeDe);

class CustomDateFormatter extends CalendarDateFormatter {
    public dayViewHour({ date, locale }: DateFormatterParams): string {
        return new DatePipe(locale).transform(date, 'HH:mm', locale);
    }

    public weekViewHour({ date, locale }: DateFormatterParams): string {
        return this.dayViewHour({ date, locale });
    }
}
/* in your component that uses the calendar
    providers: [{  provide: CalendarDateFormatter, useClass: CustomDateFormatter }]
 */

@Component({
    selector: 'app-calendar',
    templateUrl: './calendar.component.html',
    styleUrls: ['./calendar.component.scss'],
    providers: [
        { provide: CalendarDateFormatter, useClass: CustomDateFormatter },
    ],
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
