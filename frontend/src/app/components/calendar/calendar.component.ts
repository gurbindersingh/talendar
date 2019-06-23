import { Component, OnInit } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import { Router } from '@angular/router';
import localeDe from '@angular/common/locales/de-AT';

import { CalendarDateFormatter, CalendarView } from 'angular-calendar';
import { NgbModal, NgbCarouselConfig } from '@ng-bootstrap/ng-bootstrap';

import { BREAKPOINTS } from 'src/app/utils/Breakpoints';
import { ClickedDateService } from 'src/app/services/clicked-date.service';
import { CustomDateFormatter } from './CustomDateFormatter';
import { Event } from 'src/app/models/event';
import { EventClient } from 'src/app/rest/event-client';
import { EventImportService } from 'src/app/services/event-import.service';
import { MetaEvent } from './MetaEvent';
import { Trainer } from 'src/app/models/trainer';
import { TrainerClient } from 'src/app/rest/trainer-client';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { UserDetails } from 'src/app/models/user-details';
import { Authorities } from 'src/app/models/enum/authorities';
import { SessionStorageService } from 'src/app/services/session-storage.service';
import { promise } from 'protractor';
import { ImageClient } from 'src/app/rest/image-client';
import { Room } from 'src/app/models/enum/room';
import { NotificationService } from 'src/app/services/notification.service';

/**
 * In order to display week days in German the locale data
 * needs to be imported and registered, since the calender
 * uses Angular's i18n API.
 */
registerLocaleData(localeDe);

@Component({
    selector: 'app-calendar',
    templateUrl: './calendar.component.html',
    styleUrls: ['./calendar.component.scss'],
    providers: [
        { provide: CalendarDateFormatter, useClass: CustomDateFormatter },
        [NgbCarouselConfig],
    ],
})
export class CalendarComponent implements OnInit {
    calendarView = CalendarView;
    dayEndHour = 20;
    dayEndMinute = 0;
    daysInWeek: 1 | 3 | null = null;
    dayStartHour = 8;
    dayStartMinute = 0;
    // list of all loaded events
    allEvents: Event[] = [];
    // list of all events relative to the applied filters
    filteredEvents: Event[] = [];
    hourSegments = 4;
    locale = 'de-AT';
    precision = 'minutes';
    view = CalendarView.Week;
    viewDate = new Date();
    weekStartsOn = 1;
    // toggle between collapsed and open filter menu
    isCollapsed = true;
    clickedEvent: Event;

    // simple helper to map events room name to actual name for ui
    eventLocation: string;

    images: any[] = [];
    // filter specific content

    // filterable values
    rooms: any[] = [
        { name: 'Kein Filter', value: undefined },
        { name: 'Grün', value: 'Green' },
        { name: 'Orange', value: 'Orange' },
        { name: 'Erdgeschoss', value: 'GroundFloor' },
    ];
    visitorEventModalEntries = [
        {
            name: 'Beratungstermin vereinbaren',
            type: 'consultation',
        },
        { name: 'Geburtstag buchen', type: 'birthday' },
        { name: 'Raum mieten', type: 'rent' },
    ];
    trainerEventModalEntries = [
        { name: 'Neuen Kurs', type: 'course' },
        { name: 'Urlaub', type: 'holiday' },
        {
            name: 'Beratungstermin',
            type: 'consultation',
        },
        { name: 'Geburtstag', type: 'birthday' },
        // { name: 'Raummiete', type: 'rent' },
    ];
    eventTypes: any[] = [
        { name: 'Kein Filter', value: undefined },
        { name: 'Kurs', value: 'Course' },
        { name: 'Beratung', value: 'Consultation' },
        { name: 'Geburtstag', value: 'Birthday' },
        { name: 'Miete', value: 'Rent' },
    ];
    bdTypes: any[] = [
        { name: 'Kein Filter', value: undefined },
        { name: 'Trockeneis Geburtstag', value: 'DryIce' },
        { name: 'Raketen Geburtstag', value: 'Rocket' },
        { name: 'Superhelden Geburtstag', value: 'Superhero' },
        { name: 'Photo Geburtstag', value: 'Photo' },
        { name: 'Malen Geburtstag', value: 'Painting' },
    ];

    trainerList: string[] = [];
    trainers: Trainer[] = [];

    // selected value for filtering (empty initialization for ngModel Binding)
    roomSelection: { name: string; value: string };
    eventTypeSelection: { name: string; value: string };
    bdTypeSelection: { name: string; value: string };
    trainerSelection: string;
    minAgeFilter: number;
    maxAgeFilter: number;
    // decides whether we show all events or only the ones that a trainer hosts by himself
    // only (for admin/trainer)
    isPersonalView: boolean;

    // status of user
    userStatus: Authorities;

    constructor(
        private eventClient: EventClient,
        private imageCLient: ImageClient,
        private trainerClient: TrainerClient,
        private eventImport: EventImportService,
        private modalService: NgbModal,
        private router: Router,
        private dateService: ClickedDateService,
        public authService: AuthenticationService,
        private sessionService: SessionStorageService,
        private notificationService: NotificationService
    ) {
        if (screen.width < BREAKPOINTS.medium) {
            this.daysInWeek = 3;
        } else if (screen.width < BREAKPOINTS.small) {
            this.daysInWeek = 1;
        }

        this.notificationService.loginStatusChanges$.subscribe((update) => {
            this.ngOnInit();
        });
    }

    ngOnInit() {
        // anyone who is not logged cannot see every event
        if (this.authService.isLoggedIn === false) {
            // set role (will affect filter options)
            this.userStatus = Authorities.UNAUTHENTICATED;
            this.eventTypeSelection = { name: 'Kurs', value: 'Course' };

            this.eventClient
                .getAllEvents_clientView()
                .subscribe((data: Event[]) => {
                    this.allEvents = this.eventImport.mapEventsToCalendar(data);
                    this.filteredEvents = this.allEvents;
                });
        } else {
            // if logged in, check role of user and show appropriate details (== extended view)
            this.authService.getUserDetails().subscribe(
                (auth: UserDetails) => {
                    if (auth.roles.includes(Authorities.ADMIN)) {
                        this.userStatus = Authorities.AUTHENTICATED;
                        // admin start with 'all view' per default
                        this.isPersonalView = false;

                        this.eventClient
                            .getAllEvents_adminView()
                            .subscribe((data: Event[]) => {
                                this.allEvents = this.eventImport.mapEventsToCalendar(
                                    data
                                );
                                this.filteredEvents = this.allEvents;
                            });
                    } else if (auth.roles.includes(Authorities.TRAINER)) {
                        this.userStatus = Authorities.AUTHENTICATED;
                        // trainer start with 'personal events view' per default
                        this.isPersonalView = true;
                        const userID = this.sessionService.userId;

                        this.eventClient
                            .getAllEvents_trainerView(userID)
                            .subscribe((data: Event[]) => {
                                this.allEvents = this.eventImport.mapEventsToCalendar(
                                    data
                                );
                                this.filteredEvents = this.allEvents;
                            });
                    }
                },
                (error: Error) => {
                    console.log(error.message);
                    this.authService.logout();
                    this.router.navigateByUrl('/login');
                }
            );
        }
        /*         this.eventClient.getAllEvents().subscribe((data: Event[]) => {
            this.allEvents = this.eventImport.mapEventsToCalendar(data);
            this.filteredEvents = this.allEvents;
        });

 */     this.trainerClient
            .getAll()
            .subscribe((data: Trainer[]) => {
                this.trainers = data;

                this.trainerList.push(undefined);
                for (const trainer of this.trainers) {
                    const name = trainer.firstName + ' ' + trainer.lastName;
                    this.trainerList.push(name);
                }

                // explicit undefined value matches option 'reset'
            });
    }

    getEventModalEntries() {
        if (this.authService.isLoggedIn) {
            return this.trainerEventModalEntries;
        } else {
            return this.visitorEventModalEntries;
        }
    }

    showDetails(event: Event, detailsModal: any) {
        console.warn(event);

        console.log(event.pictures);

        this.images = [];
        const promises: Promise<string>[] = [];

        if (event.pictures != null) {
            for (const picture of event.pictures) {
                promises.push(
                    this.imageCLient.getCoursePicture(picture).toPromise()
                );
            }

            Promise.all(promises).then((data: string[]) => {
                this.images = new Array(data.length);
                for (let i = 0; i < data.length; i++) {
                    const blob = new Blob([data[i]]);
                    this.extractBinaryDataFromFile(blob, i);
                }
            });
        }

        // a user only sees an event (like rent, birthday (of others))
        // as reserved and will not be able to see further details
        if (event.redacted === true) {
            return;
        }

        const room = event.roomUses[0].room;
        if (room.includes(Room.Green)) {
            this.eventLocation = 'Grüner Raum';
        }
        if (room.includes(Room.Orange)) {
            this.eventLocation = 'Oranger Raum';
        }
        if (room.includes(Room.GroundFloor)) {
            this.eventLocation = 'Erdgeschoss';
        }

        if (event.eventType !== 'Rent') {
            this.clickedEvent = event;
            this.modalService.open(detailsModal, { size: 'lg' });
        }
    }

    dateClicked(date: Date, newEventModal: any) {
        console.warn(date);
        // if (date.valueOf() >= Date.now()) {
        this.dateService.setDateTime(date);
        this.modalService.open(newEventModal, { size: 'sm' });
        // }
    }

    addEvent(type: string) {
        switch (type) {
            case 'course':
                this.router.navigateByUrl('/course/add');
                break;

            case 'holiday':
                this.router.navigateByUrl('/holiday/add');
                break;

            case 'consultation':
                this.router.navigateByUrl('/consultation/add');
                break;

            case 'rent':
                this.router.navigateByUrl('/rent');
                break;

            case 'birthday':
                this.router.navigateByUrl('/birthday/book');
                break;

            default:
                break;
        }
    }

    /**
     *
     */
    toggleView(view: CalendarView, daysInWeek: 1 | 3 | null = null) {
        this.view = view;
        this.daysInWeek = daysInWeek;
    }

    /**
     * For authenticated users: change view of all events between 'complete data for all events'
     * and a view where complete data is only shown for events that are held by the currently
     * logged in trainer/admin.
     */
    public changeView(): void {
        console.log(this.isPersonalView);
        this.authService.getUserDetails().subscribe(
            (status: UserDetails) => {
                if (
                    status.roles.includes(Authorities.ADMIN) ||
                    status.roles.includes(Authorities.TRAINER)
                ) {
                    if (!this.isPersonalView) {
                        this.eventClient
                            .getAllEvents_adminView()
                            .subscribe((data: Event[]) => {
                                this.allEvents = this.eventImport.mapEventsToCalendar(
                                    data
                                );
                                this.filteredEvents = this.allEvents;
                            });
                    } else {
                        const userID = this.sessionService.userId;
                        this.eventClient
                            .getAllEvents_trainerView(userID)
                            .subscribe((data: Event[]) => {
                                this.allEvents = this.eventImport.mapEventsToCalendar(
                                    data
                                );
                                this.filteredEvents = this.allEvents;
                            });
                    }
                }
            },
            (error: Error) => {
                console.log(error.message);
                this.authService.logout();
                this.router.navigateByUrl('/login');
            }
        );
    }

    /**
     * Reset the list of all events and then apply each filter that has been specified.
     */
    public updateView(): void {
        this.filteredEvents = this.allEvents;

        this.filteredEvents = this.filteredEvents.filter((event: Event) => {
            // vars are true if filter for this context was set in GUI
            const hasRoomFilter: boolean =
                this.roomSelection !== undefined &&
                this.roomSelection.value !== undefined;
            const hasTrainerFilter: boolean =
                this.trainerSelection !== undefined;
            const hasTypeFilter: boolean =
                this.eventTypeSelection !== undefined &&
                this.eventTypeSelection.value !== undefined;
            const hasCourseFilter =
                hasTypeFilter && this.eventTypeSelection.name === 'Kurs';
            const hasBirthdayTypeSelection =
                hasTypeFilter &&
                this.bdTypeSelection !== undefined &&
                this.bdTypeSelection.value !== undefined;

            // check if given filters satisfy given event properties
            // iff filter doesnt match (ret false) remove this elem from array
            if (hasRoomFilter) {
                if (
                    this.roomSelection.value !==
                    event.roomUses[0].room.toString()
                ) {
                    return false;
                }
            }

            /**
             * Now check if this event is marked as reserved
             * If yes, then we can not expect that any data except room and time
             * are set.
             * I.e. Stop filtering!
             */
            if (event.redacted) {
                return true;
            }

            // this filter is not apllicable to events of type 'rent'
            // as rents have no assigned trainer (null)
            if (hasTrainerFilter) {
                // if event without trainer then it is a rent, filter it
                if (event.trainer === null || event.trainer === undefined) {
                    return false;
                }

                const trainerNameOfEvent =
                    event.trainer.firstName + ' ' + event.trainer.lastName;

                if (this.trainerSelection !== trainerNameOfEvent) {
                    return false;
                }
            }

            if (hasTypeFilter) {
                if (
                    this.eventTypeSelection.value !== event.eventType.toString()
                ) {
                    return false;
                }
            }

            if (hasCourseFilter) {
                if (
                    this.minAgeFilter !== null &&
                    event.minAge < this.minAgeFilter
                ) {
                    return false;
                }

                if (
                    this.maxAgeFilter !== null &&
                    event.maxAge > this.maxAgeFilter
                ) {
                    return false;
                }
            }

            // only possible if hasTypeSelection is set to 'Geburtstag'
            if (hasBirthdayTypeSelection) {
                if (this.bdTypeSelection.value !== event.birthdayType) {
                    return false;
                }
            }

            return true;
        });
    }

    /**
     * This method can be used to extract the content of a file as binary data.
     * I.e. <img src"..."> can display images given their binary representation.
     *
     * @param file the wrapper of the content. 'File' can be also used as param as
     *             it extends Blob!
     * @param index the place where this data shall be inserted in the given data source
     */
    private extractBinaryDataFromFile(file: Blob, index: number): void {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onloadend = () => {
            this.images[index] = reader.result;
        };
    }

    public routeToCourseSign(): void {
        this.router.navigateByUrl('/course/sign?id=' + this.clickedEvent.id);
    }
}
