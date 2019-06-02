import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import {
    BirthdayComponent,
    CalendarComponent,
    CancelEventComponent,
    ConsultationComponent,
    CourseComponent,
    CourseViewComponent,
    HolidayComponent,
    MeetingComponent,
    TrainerComponent,
    TrainerListComponent,
} from './components';
import { CourseSignComponent } from './components/course-sign/course-sign.component';

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
        path: 'cancelEvent',
        component: CancelEventComponent,
    },
    {
        path: 'courseView',
        component: CourseViewComponent,
    },
    {
        path: 'courseSign',
        component: CourseSignComponent,
    },
    {
        // This catch-all route should always be the LAST!
        path: '**',
        redirectTo: '',
    },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {}
