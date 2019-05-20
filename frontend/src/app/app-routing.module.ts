import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { CalendarComponent } from './calendar/calendar.component';
import { NewBirthdayComponent } from './new-birthday/new-birthday.component';

const routes: Routes = [
    {
        path: '',
        component: CalendarComponent,
    },
    {
        path: 'addbirthday',
        component: NewBirthdayComponent,

    }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {}
