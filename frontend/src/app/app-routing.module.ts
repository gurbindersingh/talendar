import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { CalendarComponent } from './calendar/calendar.component';
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
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule],
})
export class AppRoutingModule {}
