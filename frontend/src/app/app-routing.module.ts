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
    LoginComponent,
    MeetingComponent,
    TrainerComponent,
    TrainerListComponent,
} from './components';
import { CourseSignComponent } from './components/course-sign/course-sign.component';

const routes: Routes = [
    { path: 'calendar', component: CalendarComponent },
    { path: 'birthday/book', component: BirthdayComponent },
    { path: 'consultation/add', component: ConsultationComponent },
    { path: 'course/add', component: CourseComponent },
    { path: 'course/sign', component: CourseSignComponent },
    { path: 'course/view', component: CourseViewComponent },
    { path: 'event/cancel', component: CancelEventComponent },
    { path: 'holiday/add', component: HolidayComponent },
    { path: 'rent', component: MeetingComponent },
    { path: 'trainer/add', component: TrainerComponent },
    { path: 'trainer/list', component: TrainerListComponent },
    {
        // This catch-all route should always be the LAST!
        path: '**',
        redirectTo: 'calendar',
    },
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {}
