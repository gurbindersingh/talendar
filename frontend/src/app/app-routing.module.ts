import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { CalendarComponent } from './calendar/calendar.component';
import { NewBirthdayComponent } from './new-birthday/new-birthday.component';
import { AddTrainerComponent } from './add-trainer/add-trainer.component';


const routes: Routes = [
    {
        path: '',
        component: CalendarComponent,
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
