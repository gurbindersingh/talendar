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
import { SafeServerImagePipe } from 'src/app/pipes/safe-server-image-pipe';
import { httpInterceptorProviders } from './http-interceptors';
import { SessionStorageService } from './services';
import { SimpleHttpInterceptor } from './http-interceptors/simple-http-interceptor';
import { DateTimeParserService } from './services';
import { AdminGuard } from './guards/admin-guard';
import { TrainerGuard } from './guards/trainer-guard';
import { AuthenticatedGuard } from './guards/authenticated-guard';
import { InfoClient } from './rest/info-client';
import { ImageClient } from './rest/image-client';
import { CreateBirthdayTypeComponent } from './components/create-birthday-type/create-birthday-type.component';
import { BirthdayClient } from './rest/birthday-client';
import { BirthdayTypeViewComponent } from './components/birthday-type-view/birthday-type-view.component';
import {
    TrainerClient,
    TagClient,
    EventClient,
    HolidayClient,
    HolidaysClient,
    AuthenticationClient,
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
        CreateBirthdayTypeComponent,
        BirthdayTypeViewComponent,
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
        BirthdayClient

    ],
    bootstrap: [AppComponent],
    entryComponents: [NgbdModalConfirm],
})
export class AppModule { }
