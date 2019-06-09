import { Injectable } from '@angular/core';
import { MetaEvent } from '../components/calendar/MetaEvent';
import { Event } from '../models/event';
import { RoomUse } from '../models/roomUse';

@Injectable({
    providedIn: 'root',
})
export class EventImportService {
    constructor() {}

    public mapEventsToCalendar(events: Event[]): MetaEvent[] {
        const metaEvents: MetaEvent[] = [];

        for (const event of events) {
            const metaEvent: MetaEvent = new MetaEvent();
            const roomOfEvent: RoomUse[] = event.roomUses;
            const start: Date = new Date(roomOfEvent[0].begin);
            const end: Date = new Date(roomOfEvent[0].end);

            metaEvent.id = event.id;
            metaEvent.title = event.name;

            // Calendar Events' specific properties (style, assigned actions etc)
            // this set up is not completed -> consider what has to be set...
            metaEvent.allDay = false;
            metaEvent.draggable = false;

            metaEvent.start = start;
            metaEvent.end = end;
            metaEvent.event = event;

            metaEvents.push(metaEvent);
        }

        return metaEvents;
    }
}
