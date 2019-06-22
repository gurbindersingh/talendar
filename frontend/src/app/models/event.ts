import { Trainer } from './trainer';
import { RoomUse } from './roomUse';
import { Customer } from './customer';
import { EventType } from './enum/eventType';
import { CalendarEvent } from 'calendar-utils';

export class Event implements CalendarEvent {
    // From CalenderEvent
    id: number;
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
    // Custom properties
    name: string;
    roomUses: RoomUse[];
    created: Date;
    updated: Date;
    eventType: EventType;
    customerDtos: Customer[];
    // used by non rent types
    trainer: Trainer;
    // birthday specific
    headcount: number;
    ageToBe: number;
    birthdayType: string;
    // course specific
    endOfApplication: string;
    price: number;
    maxParticipants: number;
    description: string;
    minAge: number;
    maxAge: number;
    pictures: string[];
    // signals whether the data of this event had been reset for pricacy reasons
    redacted: boolean;
    // signals that this event shall be displayed subordinate to other events
    hide: boolean;
}
