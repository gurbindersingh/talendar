import { Injectable } from '@angular/core';
import { Event } from '../models/event';
import { RoomUse } from '../models/roomUse';

@Injectable({
    providedIn: 'root',
})
export class EventImportService {
    constructor() {}

    public mapEventsToCalendar(events: Event[]) {
        for (const event of events) {
            const roomOfEvent: RoomUse[] = event.roomUses;

            event.start = new Date(roomOfEvent[0].begin);
            event.end = new Date(roomOfEvent[0].end);
            event.title = event.name;

            // Calendar Events' specific properties (style, assigned actions etc)
            // this set up is not completed -> consider what has to be set...
            event.allDay = false;
            event.draggable = false;
        }
        return events;
    }
}
