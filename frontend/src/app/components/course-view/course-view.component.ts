import { Component, OnInit } from '@angular/core';
import { EventClient } from '../../rest/event-client';
import { Event } from '../../models/event';
import { NgForm } from '@angular/forms';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ResourceLoader } from '@angular/compiler';

@Component({
    selector: 'ngbd-modal-confirm',
    template:
        '<div id= "modal-container" class = "hidden"> <div class = "modal-footer"><h2>Component inside modal</h2></div>  <div class = "modal-body"> <input class="btn btn-danger col-lg-3" id = "confirmDelete" (click) = "deleteCourse(selectedEvent.id)" value = "Löschen" /> <input class="btn btn-primary col-lg-3" id = "abbrechen" (click) = "modal.close()" value = "Abbrechen" /> </div> </div>  <div id=”overlay”></div>',
})
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
