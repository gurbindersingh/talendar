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
    RentComponent,
    LoginComponent,
    TrainerComponent,
    TrainerListComponent,
} from './components';
import { CourseSignComponent } from './components/course-sign/course-sign.component';
import { TrainerGuard } from './guards/trainer-guard';
import { AuthenticatedGuard } from './guards/authenticated-guard';
import { AdminGuard } from './guards/admin-guard';
import { TagComponent } from './components/tag/tag.component';
import { CancelNewsletterComponent } from './components/cancel-newsletter/cancel-newsletter.component';

const routes: Routes = [
    { path: 'calendar', component: CalendarComponent },
    { path: 'birthday/book', component: BirthdayComponent },
    {
        path: 'consultation/add',
        component: ConsultationComponent,
    },
    {
        path: 'course/add',
        component: CourseComponent,
        canActivate: [AuthenticatedGuard],
    },
    { path: 'course/sign', component: CourseSignComponent },
    {
        path: 'course/view',
        component: CourseViewComponent,
        canActivate: [AdminGuard],
    },
    { path: 'event/cancel', component: CancelEventComponent },
    {
        path: 'holiday/add',
        component: HolidayComponent,
        canActivate: [TrainerGuard],
    },
    { path: 'rent', component: RentComponent },
    {
        path: 'trainer/add',
        component: TrainerComponent,
        canActivate: [AdminGuard],
    },
    {
        path: 'trainer/list',
        component: TrainerListComponent,
        canActivate: [AdminGuard],
    },
    { path: 'login', component: LoginComponent },
    {
        path: 'tag', component: TagComponent
    },
    {
        path: 'cancelsubscription', component: CancelNewsletterComponent
    },
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
export class AppRoutingModule { }
