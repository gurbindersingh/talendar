import { CalendarEvent } from 'calendar-utils';
import { Event } from '../../models/event';

export class MetaEvent implements CalendarEvent {
    id?: string | number;
    start: Date;
    end?: Date;
    title: string;
    color?: import('calendar-utils').EventColor;
    actions?: import('calendar-utils').EventAction[];
    allDay?: boolean;
    cssClass?: string;
    resizable?: { beforeStart?: boolean; afterEnd?: boolean };
    draggable?: boolean;
    meta?: any;

    /**
     * Note: currently event is wrapped in this class instead of let the model/event.ts
     * implement CalendarEvent, because all the properties would have to be equally named
     * as the DTOs' properties in the backend (else mapping problems)
     *
     * To have less adaption work ==> this solution
     * plus, i dont know if likely, but if they'd change the interface' proeprties sometimes
     * the jackson mapping would be fucked
     */

    // wrapped eventc content (rent, course, bday, ...)
    event: Event;
}
