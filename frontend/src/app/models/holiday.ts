import { NativeDateTimeModule } from 'ng-pick-datetime/date-time/adapter/native-date-time.module';

export class Holiday {
    constructor(
        public id: number,
        public trainer: number,
        public holidayStart: NativeDateTimeModule,
        public holidayEnd: NativeDateTimeModule,
    ) { }
}
