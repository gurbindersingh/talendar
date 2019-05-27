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
} from './components';
import { CancelEventComponent } from './components/cancel-event/cancel-event.component';
import { CourseViewComponent } from './components/course-view/course-view.component';


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
    {
        path: 'cancelEvent',
        component: CancelEventComponent
    },
    {
        path: 'courseView',
        component: CourseViewComponent
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {}
