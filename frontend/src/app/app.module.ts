import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';

import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { AppComponent } from './app.component';
import {
    CalendarComponent,
    BirthdayComponent,
    CourseComponent,
    HolidayComponent,
    MeetingComponent,
    NavigationComponent,
    TrainerComponent,
    ConsultationComponent,
    TrainerListComponent,
} from 'src/app/components';

import { httpInterceptorProviders } from './http-interceptors';
import { SessionStorageService } from './services/session-storage-service';
import { SimpleHttpInterceptor } from './http-interceptors/simple-http-interceptor';
import { TrainerClient } from './rest/trainer-client';
import { EventClient } from './rest/event-client';
import { DateTimeParserService } from './services/date-time-parser.service';
import { HolidayClient } from 'src/app/rest/holiday-client';
import { CancelEventComponent } from './components/cancel-event/cancel-event.component';
import { CourseViewComponent } from './components/course-view/course-view.component';
import { CourseSignComponent } from './components/course-sign/course-sign.component';
import { LoginComponent } from './components/login/login.component';
import { HolidaysClient } from 'src/app/rest/holidays-client';
import { AuthenticationClient } from './rest/authentication-client';

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
        ConsultationComponent,
        TrainerListComponent,
        CancelEventComponent,
        CourseViewComponent,
        CourseSignComponent,
        LoginComponent,
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
        HttpClientModule,
    ],
    providers: [
        httpInterceptorProviders,
        SessionStorageService,
        SimpleHttpInterceptor,
        TrainerClient,
        EventClient,
        DateTimeParserService,
        HolidayClient,
        HolidaysClient,
        AuthenticationClient,
    ],
    bootstrap: [AppComponent],
})
export class AppModule {}
