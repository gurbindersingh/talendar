import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { CalendarComponent } from './calendar/calendar.component';
import { BirthdayComponent } from './birthday/birthday.component';
import { CourseComponent } from './course/course.component';
import { HolidayComponent } from './holiday/holiday.component';
import { MeetingComponent } from './meeting/meeting.component';
import { TrainerComponent } from './trainer/trainer.component';

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
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {}
