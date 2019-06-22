import { Injectable } from '@angular/core';

import { Event } from '../models/event';
import { EventType } from '../models/enum/eventType';
import { RoomUse } from '../models/roomUse';

import * as Colors from '../utils/Colors';

@Injectable({
    providedIn: 'root',
})
export class EventImportService {
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
            event.cssClass = 't-event-color';

            if (event.redacted || event.hide) {
                event.color = Colors.Reserved;
                continue;
            }

            switch (event.eventType) {
                case EventType.Birthday:
                    event.color = Colors.Birthday;
                    break;

                case EventType.Course:
                    event.color = Colors.Course;
                    break;

                case EventType.Consultation:
                    event.color = Colors.Consultation;
                    break;

                case EventType.Holiday:
                    event.color = Colors.Holiday;
                    break;

                case EventType.Rent:
                    event.color = Colors.Rent;
                    break;

                default:
                    break;
            }
        }
        return events;
    }
}
