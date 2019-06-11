import { Component, OnInit } from '@angular/core';
import { EventClient } from '../../rest/event-client';
import { Event } from '../../models/event';
import { NgForm } from '@angular/forms';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ResourceLoader } from '@angular/compiler';

@Component({
    selector: 'app-course-view',
    templateUrl: './course-view.component.html',
    styleUrls: ['./course-view.component.scss'],
})
export class CourseViewComponent implements OnInit {
    constructor(
        private eventClient: EventClient,
        private modalService: NgbModal
    ) {}

    private eventList: Event[] = [];
    private title: String = 'Kursansicht';
    private selectedEvent: Event;

    openModal(event: Event, name: string) {
        this.selectedEvent = event;
        // this.modalService.open('popup');
    }

    public deleteCourse(id: number): void {
        console.log('Deleting Course with id ' + id);
        this.eventClient
            .cancelEvent(id)
            .subscribe(
                (result) => console.log(result),
                (error) => console.log(error)
            );
    }

    ngOnInit() {
        console.log('Init Event List');
        this.eventClient.getAllFutureCourses().subscribe(
            (courses: Event[]) => {
                this.eventList = courses;
            },
            (error) => {
                console.log(error);
            }
        );
    }
}
