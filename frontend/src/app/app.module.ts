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
    RentComponent,
    NavigationComponent,
    TrainerComponent,
    ConsultationComponent,
    TrainerListComponent,
    NgbdModalConfirm,
} from 'src/app/components';

import { httpInterceptorProviders } from './http-interceptors';
import { SessionStorageService } from './services/session-storage.service';
import { SimpleHttpInterceptor } from './http-interceptors/simple-http-interceptor';
import { TrainerClient } from './rest/trainer-client';
import { TagClient } from './rest/tag-client';
import { EventClient } from './rest/event-client';
import { DateTimeParserService } from './services/date-time-parser.service';
import { HolidayClient } from 'src/app/rest/holiday-client';
import { CancelEventComponent } from './components/cancel-event/cancel-event.component';
import { CourseViewComponent } from './components/course-view/course-view.component';
import { CourseSignComponent } from './components/course-sign/course-sign.component';
import { LoginComponent } from './components/login/login.component';
import { HolidaysClient } from 'src/app/rest/holidays-client';
import { AuthenticationClient } from './rest/authentication-client';
import { AdminGuard } from './guards/admin-guard';
import { TrainerGuard } from './guards/trainer-guard';
import { AuthenticatedGuard } from './guards/authenticated-guard';
import { InfoComponent } from './components/info/info.component';
import { InfoClient } from './rest/info-client';
import { ImageClient } from './rest/image-client';
import { SafeServerImagePipe } from './pipes/safe-server-image-pipe';
import { TagComponent } from './components/tag/tag.component';
import { CancelNewsletterComponent } from './components/cancel-newsletter/cancel-newsletter.component';
import { CreateBirthdayTypeComponent } from './components/create-birthday-type/create-birthday-type.component';
import { BirthdayClient } from './rest/birthday-client';
import { BirthdayTypeViewComponent } from './components/birthday-type-view/birthday-type-view.component';

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
