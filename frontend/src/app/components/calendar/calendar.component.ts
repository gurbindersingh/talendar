import { Component, OnInit } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import localeDe from '@angular/common/locales/de-AT';

import { CalendarDateFormatter, CalendarView } from 'angular-calendar';

import { BREAKPOINTS } from 'src/app/utils/Breakpoints';
import { CustomDateFormatter } from './CustomDateFormatter';
import { Event } from '../../models/event';
import { EventClient } from 'src/app/rest/event-client';
import { EventImportService } from 'src/app/services/event-import.service';
import { MetaEvent } from './MetaEvent';
import { Trainer } from 'src/app/models/trainer';
import { TrainerClient } from 'src/app/rest/trainer-client';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';

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
    allEvents: MetaEvent[] = [];
    // list of all events relative to the applied filters
    filteredEvents: MetaEvent[] = [];
    hourSegments = 4;
    locale = 'de-AT';
    precision = 'minutes';
    view = CalendarView.Week;
    viewDate = new Date();
    weekStartsOn = 1;
    private clickedDateTime: Date;
    // toggle between collapsed and open filter menu
    isCollapsed = true;

    // filter specific content

    // filterable values
    rooms: any[] = [
        { name: 'Grün', value: 'Green' },
        { name: 'Orange', value: 'Orange' },
        { name: 'Erdgeschoss', value: 'GroundFloor' },
        { name: 'Kein Filter', value: undefined },
    ];
    eventTypes: any[] = [
        { name: 'Kurs', value: 'Course' },
        { name: 'Beratung', value: 'Consultation' },
        { name: 'Geburtstag', value: 'Birthday' },
        { name: 'Miete', value: 'Rent' },
        { name: 'Kein Filter', value: undefined },
    ];
    bdTypes: any[] = [
        { name: 'Trockeneis Geburtstag', value: 'DryIce' },
        { name: 'Raketen Geburtstag', value: 'Rocket' },
        { name: 'Superhelden Geburtstag', value: 'Superhero' },
        { name: 'Photo Geburtstag', value: 'Photo' },
        { name: 'Malen Geburtstag', value: 'Painting' },
        { name: 'Kein Filter', value: undefined },
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

    constructor(
        private eventClient: EventClient,
        private trainerClient: TrainerClient,
        private eventImport: EventImportService,
        private modalService: NgbModal,
        private router: Router
    ) {
        if (screen.width < BREAKPOINTS.medium) {
            this.daysInWeek = 3;
        } else if (screen.width < BREAKPOINTS.small) {
            this.daysInWeek = 1;
        }
    }

    ngOnInit() {
        this.eventClient.getAllEvents().subscribe((data: Event[]) => {
            this.allEvents = this.eventImport.mapEventsToCalendar(data);
            this.filteredEvents = this.allEvents;
        });

        this.trainerClient.getAll().subscribe((data: Trainer[]) => {
            this.trainers = data;

            for (const trainer of this.trainers) {
                const name = trainer.firstName + ' ' + trainer.lastName;
                this.trainerList.push(name);
            }

            // explicit undefined value matches option 'reset' (if clicked selection is resetted)
            this.trainerList.push(undefined);
        });
    }

    showDetails(event: any) {
        console.warn(event);
        alert('Implement showDetails()');
    }

    dateClicked(date: Date, content: any) {
        console.warn(date);
        this.clickedDateTime = date;
        this.modalService.open(content);
    }

    addEvent(type: string) {
        switch (type) {
            case 'new-course':
                this.router.navigateByUrl(
                    '/course/add?date=' + this.clickedDateTime
                );
                break;

            case 'new-holiday':
                this.router.navigateByUrl(
                    '/holiday/add?date=' + this.clickedDateTime
                );
                break;
            case 'new-consultation':
                this.router.navigateByUrl(
                    '/consultation/add?date=' + this.clickedDateTime
                );
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

    public updateView(): void {
        this.filteredEvents = this.allEvents;

        this.filteredEvents = this.filteredEvents.filter((event: MetaEvent) => {
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
                    event.event.roomUses[0].room.toString()
                ) {
                    return false;
                }
            }

            // this filter is not apllicable to events of type 'rent'
            // as rents have no assigned trainer (null)
            if (hasTrainerFilter) {
                // if event without trainer then it is a rent, filter it
                if (
                    event.event.trainer === null ||
                    event.event.trainer === undefined
                ) {
                    return false;
                }

                const trainerNameOfEvent =
                    event.event.trainer.firstName +
                    ' ' +
                    event.event.trainer.lastName;

                if (this.trainerSelection !== trainerNameOfEvent) {
                    return false;
                }
            }

            if (hasTypeFilter) {
                if (
                    this.eventTypeSelection.value !==
                    event.event.eventType.toString()
                ) {
                    return false;
                }
            }

            if (hasCourseFilter) {
                if (
                    this.minAgeFilter !== null &&
                    event.event.minAge < this.minAgeFilter
                ) {
                    return false;
                }

                if (
                    this.maxAgeFilter !== null &&
                    event.event.maxAge > this.maxAgeFilter
                ) {
                    return false;
                }
            }

            // only possible if hasTypeSelection is set to 'Geburtstag'
            if (hasBirthdayTypeSelection) {
                if (this.bdTypeSelection.value !== event.event.birthdayType) {
                    return false;
                }
            }

            return true;
        });
    }
}
