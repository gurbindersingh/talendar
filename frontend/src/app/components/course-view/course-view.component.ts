import { Component, OnInit, EventEmitter } from '@angular/core';
import { EventClient } from '../../rest/event-client';
import { Event } from '../../models/event';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';

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

    eventList: Event[] = [];
    filteredEventList: Event[] = [];
    eventListPage: Event[] = [];
    currentPage = 1;
    itemsPerPage = 10;
    title = 'Kursansicht';
    private selectedEvent: Event;

    openModal(event: Event, name: string) {
        this.selectedEvent = event;
        // this.modalService.open('popup');
    }

    public deleteCourse(id: number): void {
        console.log('Deleting Course with id ' + id);
        this.eventClient.cancelEvent(id).subscribe(
            (result) => {
                console.log(result);
                this.ngOnInit();
            },
            (error) => console.log(error)
        );
    }

    updateListPage(page?: number) {
        this.eventListPage = this.filteredEventList.slice(
            (this.currentPage - 1) * this.itemsPerPage,
            this.currentPage * this.itemsPerPage
        );
    }

    filterList(pValue: string) {
        // Think about splitting up the string at white spaces
        const searchString = pValue
            .replace(/^\s+/, '') // Remove whitespaces at the start of the string
            .replace(/\s+$/, '') // Remove whitespaces at the end
            .replace(/\s{2,}/g, ' ') // Remove subsequent whitespaces
            .toLocaleLowerCase();

        if (searchString.length > 0 && searchString !== ' ') {
            this.filteredEventList = this.eventList.filter((event) =>
                event.name.toLocaleLowerCase().includes(searchString)
            );
        } else {
            this.filteredEventList = this.eventList;
        }
        this.updateListPage();
    }

    ngOnInit() {
        console.log('Init Event List');
        this.eventClient.getAllFutureCourses().subscribe(
            (courses: Event[]) => {
                this.eventList = courses;
                this.filteredEventList = this.eventList;
                this.updateListPage();
            },
            (error) => {
                console.log(error);
            }
        );
    }
}
