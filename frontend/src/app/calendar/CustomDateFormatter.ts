import { DatePipe } from '@angular/common';

import { CalendarDateFormatter, DateFormatterParams } from 'angular-calendar';

/**
 * Creating a custom date formatter class was necessary to
 * display the time in 24 hours format instead of AM/PM.
 * The formatter uses Angular's DatePipe API.
 */
export class CustomDateFormatter extends CalendarDateFormatter {
    public dayViewHour({ date, locale }: DateFormatterParams): string {
        return new DatePipe(locale).transform(date, 'HH:mm', locale);
    }

    public weekViewHour({ date, locale }: DateFormatterParams): string {
        return this.dayViewHour({ date, locale });
    }
}
