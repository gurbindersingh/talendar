import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { OwlDateTimeModule, OwlNativeDateTimeModule } from 'ng-pick-datetime';

import { AppComponent } from './app.component';
import { CalendarComponent } from './calendar/calendar.component';
import { AddCourseComponent } from './add-course/add-course.component';
import { NewBirthdayComponent } from './new-birthday/new-birthday.component';
import { AddPrivateMeetingComponent } from './add-private-meeting/add-private-meeting.component';
import { AddTrainerComponent } from './add-trainer/add-trainer.component';
import { AddHolidayComponent } from './add-holiday/add-holiday.component';

@NgModule({
    declarations: [AppComponent, CalendarComponent, AddCourseComponent, AddPrivateMeetingComponent, NewBirthdayComponent, AddTrainerComponent, AddHolidayComponent],
    imports: [
        AppRoutingModule,
        BrowserAnimationsModule,
        BrowserModule,
        CalendarModule.forRoot({
            provide: DateAdapter,
            useFactory: adapterFactory,
        }),
        NgbModule,
        FormsModule,
        OwlDateTimeModule,
        OwlNativeDateTimeModule,
    ],
    providers: [],
    bootstrap: [AppComponent],
})
export class AppModule {}
