import { Injectable } from '@angular/core';

import { Event } from '../models/event';
import { EventType } from '../models/enum/eventType';
import { RoomUse } from '../models/roomUse';

import * as Colors from '../utils/Colors';
import { Room } from '../models/enum/room';

@Injectable({
    providedIn: 'root',
})
export class EventImportService {
    public mapEventsToCalendar(events: Event[]) {
        for (const event of events) {
            const roomOfEvent: RoomUse[] = event.roomUses;

            if (roomOfEvent.length > 1) {
                let i = 0;
                for (const roomUse of roomOfEvent) {
                    if (i === 0) {
                        event.start = new Date(roomUse.begin);
                        event.end = new Date(roomUse.end);
                        event.title = event.name;
                    } else {
                        const event2 = Object.assign({}, event);
                        event2.roomUses = [];
                        event2.roomUses.push(roomUse);
                        events.push(event2);
                    }
                    i++;
                }
            } else {
                event.start = new Date(roomOfEvent[0].begin);
                event.end = new Date(roomOfEvent[0].end);
                event.title = event.name;
            }

            // Calendar Events' specific properties (style, assigned actions etc)
            // this set up is not completed -> consider what has to be set...
            event.allDay = false;
            event.draggable = false;
            event.cssClass = 't-event-color';

            if (event.redacted || event.hide) {
                event.color = Colors.Reserved;
                let reservedName;
                const room = event.roomUses[0].room;

                if (room.includes(Room.Green)) {
                    reservedName = 'Grüner Raum';
                }
                if (room.includes(Room.Orange)) {
                    reservedName = 'Oranger Raum';
                }
                if (room.includes(Room.GroundFloor)) {
                    reservedName = 'Erdgeschoss';
                }
                reservedName += ' reserviert';
                event.title = reservedName;

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
