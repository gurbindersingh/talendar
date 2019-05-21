import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { CalendarComponent } from './calendar/calendar.component';
import { NewBirthdayComponent } from './new-birthday/new-birthday.component';
import { AddCourseComponent } from './add-course/add-course.component';
import { AddPrivateMeetingComponent } from './add-private-meeting/add-private-meeting.component';
import { AddHolidayComponent } from './add-holiday/add-holiday.component';

const routes: Routes = [
    {
        path: '',
        component: CalendarComponent,

    },
    {
        path: 'addholiday',
        component: AddHolidayComponent,

    },
    {
        path: 'addbirthday',
        component: NewBirthdayComponent,
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
