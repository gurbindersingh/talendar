import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { CalendarComponent } from './calendar/calendar.component';
import { AddCourseComponent } from './add-course/add-course.component';
import { AddPrivateMeetingComponent } from './add-private-meeting/add-private-meeting.component';
import { NewBirthdayComponent } from './new-birthday/new-birthday.component';
import { AddTrainerComponent } from './add-trainer/add-trainer.component';


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
    },
    {
        path: 'addbirthday',
        component: NewBirthdayComponent,

    },
    {
        path: 'createTrainer',
        component: AddTrainerComponent

    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {}
