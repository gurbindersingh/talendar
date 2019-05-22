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
import { BirthdayComponent } from './birthday/birthday.component';
import { CalendarComponent } from './calendar/calendar.component';
import { NavigationComponent } from './navigation/navigation.component';

import { CourseComponent } from './course/course.component';
import { HolidayComponent } from './holiday/holiday.component';
import { MeetingComponent } from './meeting/meeting.component';
import { TrainerComponent } from './trainer/trainer.component';

@NgModule({
    declarations: [
        AppComponent,
        BirthdayComponent,
        CalendarComponent,
        CourseComponent,
        HolidayComponent,
        MeetingComponent,
        NavigationComponent,
        TrainerComponent,
    ],
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
