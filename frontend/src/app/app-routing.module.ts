import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import {
    CalendarComponent,
    ConsultationComponent,
    BirthdayComponent,
    CourseComponent,
    HolidayComponent,
    MeetingComponent,
    TrainerComponent,
    TrainerListComponent,
} from 'src/app/components';

const routes: Routes = [
    {
        path: '',
        component: CalendarComponent,
    },
    {
        path: 'holiday',
        component: HolidayComponent,
    },
    {
        path: 'consultation',
        component: ConsultationComponent,
    },
    {
        path: 'birthday',
        component: BirthdayComponent,
    },
    {
        path: 'course',
        component: CourseComponent,
    },
    {
        path: 'meeting',
        component: MeetingComponent,
    },
    {
        path: 'trainer',
        component: TrainerComponent,
    },
    {
        path: 'trainerList',
        component: TrainerListComponent,
    },
    {
        path: '*',
        component: CalendarComponent,
    },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {}
