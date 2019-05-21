import { AppRoutingModule } from './app-routing.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';


import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';


import { AppComponent } from './app.component';
import { CalendarComponent } from './calendar/calendar.component';
import { NewBirthdayComponent } from './new-birthday/new-birthday.component';
import { AddTrainerComponent } from './add-trainer/add-trainer.component';
import 'rxjs/Rx';

 

@NgModule({
    declarations: [AppComponent, CalendarComponent, NewBirthdayComponent, AddTrainerComponent],
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

    ],
    providers: [],
    bootstrap: [AppComponent],
})
export class AppModule {}
