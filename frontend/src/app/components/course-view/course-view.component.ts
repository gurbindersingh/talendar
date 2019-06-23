import { Component, OnInit, EventEmitter } from '@angular/core';
import { EventClient } from '../../rest/event-client';
import { Event } from '../../models/event';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Authorities } from 'src/app/models/enum/authorities';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { UserDetails } from 'src/app/models/user-details';

@Component({
    selector: 'app-course-view',
    templateUrl: './course-view.component.html',
    styleUrls: ['./course-view.component.scss'],
})
export class CourseViewComponent implements OnInit {
    constructor(
        private eventClient: EventClient,
        private modalService: NgbModal,
        private authenticationService: AuthenticationService
    ) {}

    eventList: Event[] = [];
    filteredEventList: Event[] = [];
    eventListPage: Event[] = [];
    currentPage = 1;
    itemsPerPage = 10;
    title = 'Kursansicht';
    searchPlaceholder = 'Nach einem Event suchen...';
    filter = '';

    eventTypeSelection: { name: string; value: string } = undefined;
    isPersonalView: boolean;

    eventTypes: any[];

    eventTypesAdmin: any[] = [
        { name: 'Kurs', value: 'Course' },
        { name: 'Beratung', value: 'Consultation' },
        { name: 'Geburtstag', value: 'Birthday' },
        { name: 'Miete', value: 'Rent' },
        { name: 'Kein Filter', value: undefined },
    ];

    eventTypesTrainer: any[] = [
        { name: 'Kurs', value: 'Course' },
        { name: 'Beratung', value: 'Consultation' },
        { name: 'Geburtstag', value: 'Birthday' },
        { name: 'Kein Filter', value: undefined },
    ];

    private selectedEvent: Event;

    private role: Authorities;

    openModal(event: Event, name: string) {
        this.selectedEvent = event;
        // this.modalService.open('popup');
    }

    public deleteCourse(id: number): void {
        console.log('Deleting Course with id ' + id);

        this.filteredEventList = this.filteredEventList.filter(
            (event) => event.id !== id
        );
        this.updateListPage();
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

    filterList() {
        // Think about splitting up the string at white spaces
        const searchString = this.filter
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

        if (this.eventTypeSelection.value !== undefined) {
            if (this.eventTypeSelection.value === 'Course') {
                this.searchPlaceholder = 'Nach einem Kurs suchen...';
            }
            if (this.eventTypeSelection.value === 'Consultation') {
                this.searchPlaceholder = 'Nach einer Beratung suchen...';
            }
            if (this.eventTypeSelection.value === 'Birthday') {
                this.searchPlaceholder = 'Nach einem Geburtstag suchen...';
            }
            if (this.eventTypeSelection.value === 'Rent') {
                this.searchPlaceholder = 'Nach Mieten suchen...';
            }
        } else {
            this.searchPlaceholder = 'Nach einem Event suchen...';
        }

        this.filteredEventList = this.filteredEventList.filter(
            (event: Event) => {
                if (this.eventTypeSelection.value === undefined) {
                    return true;
                }
                return (
                    this.eventTypeSelection.value === event.eventType.toString()
                );
            }
        );

        this.updateListPage();
    }

    /**
     * This method is invoked when switching the toggle.
     * Can only be performed by admin.
     */
    changeView(): void {
        let roleSpecificView: Authorities;
        if (this.isPersonalView) {
            roleSpecificView = Authorities.TRAINER;
        } else {
            roleSpecificView = Authorities.ADMIN;
        }

        this.eventClient.getAllFutureEvents(roleSpecificView).subscribe(
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

    ngOnInit() {
        console.log('Init Event List');

        // pre init role of this user
        this.authenticationService
            .getUserDetails()
            .subscribe((userDetails: UserDetails) => {
                if (userDetails.roles.includes(Authorities.ADMIN)) {
                    this.role = Authorities.ADMIN;
                    this.isPersonalView = false;
                    this.eventTypes = this.eventTypesAdmin;
                } else if (userDetails.roles.includes(Authorities.TRAINER)) {
                    this.role = Authorities.TRAINER;
                    this.eventTypes = this.eventTypesTrainer;
                }

                // load view with only personel events, admins can change to diff view later
                this.eventClient
                    .getAllFutureEvents(Authorities.TRAINER)
                    .subscribe(
                        (courses: Event[]) => {
                            this.eventList = courses;
                            this.filteredEventList = this.eventList;
                            this.updateListPage();
                        },
                        (error) => {
                            console.log(error);
                        }
                    );
            });
    }
}
