import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { CalendarComponent } from './calendar/calendar.component';
import { AddCourseComponent } from './add-course/add-course.component';
import { AddPrivateMeetingComponent } from './add-private-meeting/add-private-meeting.component';

const routes: Routes = [
    {
        path: '',
        component: CalendarComponent,
    },
    {
        path: 'addCourse',
        component: AddCourseComponent,
    },
    {
        path: 'addPrivateMeeting',
        component: AddPrivateMeetingComponent,
    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {}
