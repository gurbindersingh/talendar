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
    BirthdayComponent,
    CalendarComponent,
    CancelEventComponent,
    CancelNewsletterComponent,
    ConsultationComponent,
    CourseComponent,
    CourseSignComponent,
    CourseViewComponent,
    HolidayComponent,
    InfoComponent,
    LoginComponent,
    NavigationComponent,
    NgbdModalConfirm,
    RecaptchaComponent,
    RentComponent,
    TagComponent,
    TrainerComponent,
    TrainerListComponent,
} from 'src/app/components';

import { ConsultationTimeComponent } from 'src/app/components/consultation-time/consultation-time.component';

import { httpInterceptorProviders } from './http-interceptors';
import { SessionStorageService } from './services';
import { SimpleHttpInterceptor } from './http-interceptors/simple-http-interceptor';
import { DateTimeParserService } from './services';
import { AdminGuard } from './guards/admin-guard';
import { TrainerGuard } from './guards/trainer-guard';
import { AuthenticatedGuard } from './guards/authenticated-guard';
import {
    TrainerClient,
    TagClient,
    EventClient,
    HolidayClient,
    HolidaysClient,
    AuthenticationClient,
    InfoClient,
    ImageClient,
} from './rest';
import { ConsultationTimeClient } from './rest/consultationTime-client';
import { ConsultationTimesClient } from './rest/consultationTimes-client';

@NgModule({
    declarations: [
        AppComponent,
        BirthdayComponent,
        CalendarComponent,
        CourseComponent,
        HolidayComponent,
        RentComponent,
        NavigationComponent,
        CancelNewsletterComponent,
        TrainerComponent,
        ConsultationComponent,
        ConsultationTimeComponent,
        TrainerListComponent,
        CancelEventComponent,
        CourseViewComponent,
        CourseSignComponent,
        NgbdModalConfirm,
        LoginComponent,
        InfoComponent,
        SafeServerImagePipe,
        TagComponent,
        CancelNewsletterComponent,
        RecaptchaComponent,
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
        TagClient,
        EventClient,
        DateTimeParserService,
        HolidayClient,
        HolidaysClient,
        ConsultationTimeClient,
        ConsultationTimesClient,
        AuthenticationClient,
        AdminGuard,
        TrainerGuard,
        AuthenticatedGuard,
        InfoClient,
        ImageClient,
    ],
    bootstrap: [AppComponent],
    entryComponents: [NgbdModalConfirm],
})
export class AppModule {}
